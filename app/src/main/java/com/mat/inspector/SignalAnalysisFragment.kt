package com.mat.inspector

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.mat.inspector.databinding.FragmentSignalAnalysisBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SignalAnalysisFragment : Fragment() {

    private lateinit var binding: FragmentSignalAnalysisBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignalAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressBar.visibility = View.VISIBLE
        binding.layoutSignalData.visibility = View.INVISIBLE
        loadStuff()
        binding.progressBar.visibility = View.GONE
        binding.layoutSignalData.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun loadStuff() {
        val fs = 14_000.0
        val harmonicsCnt = 10
        val data = Connector.getData()

        val Ua = data[0].toDoubleArray()
        val Uafft = fft(Ua)
        val Uasd = getSignalData(fs, Uafft)

        val Ub = data[1].toDoubleArray()
        val Ubfft = fft(Ub)
        val Ubsd = getSignalData(fs, Ubfft)

        val Uc = data[2].toDoubleArray()
        val Ucfft = fft(Uc)
        val Ucsd = getSignalData(fs, Ucfft)

        val IobcA = data[3].toDoubleArray()
        val IobcAfft = fft(IobcA)
        val IobcAsd = getSignalData(fs, IobcAfft)

        val IobcB = data[4].toDoubleArray()
        val IobcBfft = fft(IobcB)
        val IobcBsd = getSignalData(fs, IobcBfft)

        val IobcC = data[5].toDoubleArray()
        val IobcCfft = fft(IobcC)
        val IobcCsd = getSignalData(fs, IobcCfft)

        val InetA = data[6].toDoubleArray()
        val InetAfft = fft(InetA)
        val InetAsd = getSignalData(fs, InetAfft)

        val InetB = data[7].toDoubleArray()
        val InetBfft = fft(InetB)
        val InetBsd = getSignalData(fs, InetBfft)

        val InetC = data[8].toDoubleArray()
        val InetCfft = fft(InetC)
        val InetCsd = getSignalData(fs, InetCfft)

        // RMS
        binding.mtvUaRms.text = "%.2f".format(rms(Ua))
        binding.mtvUbRms.text = "%.2f".format(rms(Ub))
        binding.mtvUcRms.text = "%.2f".format(rms(Uc))

        binding.mtvIaobcRms.text = "%.2f".format(rms(IobcA))
        binding.mtvIbobcRms.text = "%.2f".format(rms(IobcB))
        binding.mtvIcobcRms.text = "%.2f".format(rms(IobcC))

        binding.mtvIanetRms.text = "%.2f".format(rms(InetA))
        binding.mtvIbnetRms.text = "%.2f".format(rms(InetB))
        binding.mtvIcnetRms.text = "%.2f".format(rms(InetC))

        // FREQUENCY
        binding.mtvUaFreq.text = "%.2f".format(Uasd.frequency)
        binding.mtvUbFreq.text = "%.2f".format(Ubsd.frequency)
        binding.mtvUcFreq.text = "%.2f".format(Ucsd.frequency)

        binding.mtvIaobcFreq.text = "%.2f".format(IobcAsd.frequency)
        binding.mtvIbobcFreq.text = "%.2f".format(IobcBsd.frequency)
        binding.mtvIcobcFreq.text = "%.2f".format(IobcCsd.frequency)

        binding.mtvIanetFreq.text = "%.2f".format(InetAsd.frequency)
        binding.mtvIbnetFreq.text = "%.2f".format(InetBsd.frequency)
        binding.mtvIcnetFreq.text = "%.2f".format(InetCsd.frequency)

        // THD
        binding.mtvIaobcThd.text = "%.2f".format(thd(fs, IobcAfft, harmonicsCnt))
        binding.mtvIbobcThd.text = "%.2f".format(thd(fs, IobcBfft, harmonicsCnt))
        binding.mtvIcobcThd.text = "%.2f".format(thd(fs, IobcCfft, harmonicsCnt))

        binding.mtvIanetThd.text = "%.2f".format(thd(fs, InetAfft, harmonicsCnt))
        binding.mtvIbnetThd.text = "%.2f".format(thd(fs, InetBfft, harmonicsCnt))
        binding.mtvIcnetThd.text = "%.2f".format(thd(fs, InetCfft, harmonicsCnt))

        // Asymmetry
        binding.mtvUaAsym.text = "%.2f".format(cosShiftToSin(Uasd.shift))
        binding.mtvUbAsym.text = "%.2f".format(cosShiftToSin(Ubsd.shift))
        binding.mtvUcAsym.text = "%.2f".format(cosShiftToSin(Ucsd.shift))

        // Max value
        binding.mtvUaMax.text = "%.2f".format(Ua.maxOrNull())
        binding.mtvUbMax.text = "%.2f".format(Ub.maxOrNull())
        binding.mtvUcMax.text = "%.2f".format(Uc.maxOrNull())

        binding.mtvIaobcMax.text = "%.2f".format(IobcA.maxOrNull())
        binding.mtvIbobcMax.text = "%.2f".format(IobcB.maxOrNull())
        binding.mtvIcobcMax.text = "%.2f".format(IobcC.maxOrNull())

        binding.mtvIanetMax.text = "%.2f".format(InetA.maxOrNull())
        binding.mtvIbnetMax.text = "%.2f".format(InetB.maxOrNull())
        binding.mtvIcnetMax.text = "%.2f".format(InetC.maxOrNull())

        // Power
        val ap =
            Uasd.amplitude * IobcAsd.amplitude * cos(IobcAsd.shift) +
                    Ubsd.amplitude * IobcBsd.amplitude * cos(IobcBsd.shift) +
                    Ucsd.amplitude * IobcCsd.amplitude * cos(IobcCsd.shift)

        val appP =
            Uasd.amplitude * IobcAsd.amplitude * sin(cosShiftToSin(IobcAsd.shift)) +
                    Ubsd.amplitude * IobcBsd.amplitude * sin(cosShiftToSin(IobcBsd.shift)) +
                    Ucsd.amplitude * IobcCsd.amplitude * sin(cosShiftToSin(IobcCsd.shift))

        binding.mtvActivePower.text = "%.2f".format(ap)
        binding.mtvApparentPower.text = "%.2f".format(appP)
        binding.mtvReactivePower.text = "%.2f".format(sqrt(ap * ap + appP * appP))
    }

}