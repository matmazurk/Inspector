package com.mat.inspector

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

    private fun loadStuff() {
        val fs = 10.0
        val harmonicsCnt = 20
        val data = Connector.getData()

        val Ua = data[1].toDoubleArray()
        val Uafft = fft(Ua)
        val Uasd = getSignalData(fs, Uafft)

        val Ub = data[2].toDoubleArray()
        val Ubfft = fft(Ub)
        val Ubsd = getSignalData(fs, Ubfft)

        val Uc = data[3].toDoubleArray()
        val Ucfft = fft(Uc)
        val Ucsd = getSignalData(fs, Ucfft)

        val IobcA = data[4].toDoubleArray()
        val IobcAfft = fft(IobcA)
        val IobcAsd = getSignalData(fs, IobcAfft)

        val IobcB = data[5].toDoubleArray()
        val IobcBfft = fft(IobcB)
        val IobcBsd = getSignalData(fs, IobcBfft)

        val IobcC = data[6].toDoubleArray()
        val IobcCfft = fft(IobcC)
        val IobcCsd = getSignalData(fs, IobcCfft)

        val InetA = data[7].toDoubleArray()
        val InetAfft = fft(InetA)
        val InetAsd = getSignalData(fs, InetAfft)

        val InetB = data[8].toDoubleArray()
        val InetBfft = fft(InetB)
        val InetBsd = getSignalData(fs, InetBfft)

        val InetC = data[9].toDoubleArray()
        val InetCfft = fft(InetC)
        val InetCsd = getSignalData(fs, InetCfft)

        // RMS
        binding.mtvUaRms.text = rms(Ua).toString()
        binding.mtvUbRms.text = rms(Ub).toString()
        binding.mtvUcRms.text = rms(Uc).toString()

        binding.mtvIaobcRms.text = rms(IobcA).toString()
        binding.mtvIbobcRms.text = rms(IobcB).toString()
        binding.mtvIcobcRms.text = rms(IobcC).toString()

        binding.mtvIanetRms.text = rms(InetA).toString()
        binding.mtvIbnetRms.text = rms(InetB).toString()
        binding.mtvIcnetRms.text = rms(InetC).toString()

        // FREQUENCY
        binding.mtvUaFreq.text = Uasd.frequency.toString()
        binding.mtvUbFreq.text = Ubsd.frequency.toString()
        binding.mtvUcFreq.text = Ucsd.frequency.toString()

        binding.mtvIaobcFreq.text = IobcAsd.frequency.toString()
        binding.mtvIbobcFreq.text = IobcBsd.frequency.toString()
        binding.mtvIbobcFreq.text = IobcCsd.frequency.toString()

        binding.mtvIanetFreq.text = InetAsd.frequency.toString()
        binding.mtvIbnetFreq.text = InetBsd.frequency.toString()
        binding.mtvIcnetFreq.text = InetCsd.frequency.toString()

        // THD
        binding.mtvIaobcThd.text = thd(fs, IobcAfft, harmonicsCnt).toString()
        binding.mtvIbobcThd.text = thd(fs, IobcBfft, harmonicsCnt).toString()
        binding.mtvIcobcThd.text = thd(fs, IobcCfft, harmonicsCnt).toString()

        binding.mtvIanetThd.text = thd(fs, InetAfft, harmonicsCnt).toString()
        binding.mtvIbnetThd.text = thd(fs, InetBfft, harmonicsCnt).toString()
        binding.mtvIcnetThd.text = thd(fs, InetCfft, harmonicsCnt).toString()

        // Asymmetry
        binding.mtvUaAsym.text = cosShiftToSin(Uasd.shift).toString()
        binding.mtvUbAsym.text = cosShiftToSin(Ubsd.shift).toString()
        binding.mtvUcAsym.text = cosShiftToSin(Ucsd.shift).toString()

        // Max value
        binding.mtvUaMax.text = Ua.maxOrNull().toString()
        binding.mtvUbMax.text = Ub.maxOrNull().toString()
        binding.mtvUcMax.text = Uc.maxOrNull().toString()

        binding.mtvIaobcMax.text = IobcA.maxOrNull().toString()
        binding.mtvIbobcMax.text = IobcB.maxOrNull().toString()
        binding.mtvIcobcMax.text = IobcC.maxOrNull().toString()

        binding.mtvIanetMax.text = InetA.maxOrNull().toString()
        binding.mtvIbnetMax.text = InetB.maxOrNull().toString()
        binding.mtvIcnetMax.text = InetC.maxOrNull().toString()

        // Power
        val ap =
            Uasd.amplitude * IobcAsd.amplitude * cos(IobcAsd.shift) +
                    Ubsd.amplitude * IobcBsd.amplitude * cos(IobcBsd.shift) +
                    Ucsd.amplitude * IobcCsd.amplitude * cos(IobcCsd.shift)

        val appP =
            Uasd.amplitude * IobcAsd.amplitude * sin(cosShiftToSin(IobcAsd.shift)) +
                    Ubsd.amplitude * IobcBsd.amplitude * sin(cosShiftToSin(IobcBsd.shift)) +
                    Ucsd.amplitude * IobcCsd.amplitude * sin(cosShiftToSin(IobcCsd.shift))

        binding.mtvActivePower.text = ap.toString()
        binding.mtvApparentPower.text = appP.toString()
        binding.mtvReactivePower.text = sqrt(ap * ap + appP * appP).toString()
    }

}