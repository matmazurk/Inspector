package com.mat.inspector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mat.inspector.databinding.FragmentChartDetailsBinding

class ChartPlotFragment : Fragment() {

    val args: ChartPlotFragmentArgs by navArgs()
    private lateinit var binding: FragmentChartDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(ChartPlotFragmentDirections.actionChartDetailsFragmentToConnectionFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback);
        val data = Connector.getData()[args.chartId]
        val rms = rms(data)
        binding.mtvRms.text = rms.toString()
    }

    companion object {
        private const val SAMPLING_FREQUENCY_HZ = 2
    }

}