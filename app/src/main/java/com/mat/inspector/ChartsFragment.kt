package com.mat.inspector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mat.inspector.databinding.FragmentChartsBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.InetAddress

class ChartsFragment : Fragment() {

    private lateinit var binding: FragmentChartsBinding
    private val viewModel: ChartsViewModel by viewModel()
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()
    private val adapter: ChartsAdapter = ChartsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartsBinding.inflate(inflater, container, false)
        binding.rvCharts.adapter = adapter
        binding.rvCharts.layoutManager = LinearLayoutManager(requireActivity())
        adapter.setData(configurationViewModel.configuration.charts)
        binding.fabChartsAdd.setOnClickListener {
            NewChartDialog().show(childFragmentManager, "new chart dialog")
        }
        binding.fabChartsRun.setOnClickListener {
            val conf = configurationViewModel.configuration
            viewModel.loadCharts(conf.serverAddress, conf.port)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configurationViewModel.configurationLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it.charts)
        }
        viewModel.chartsLoaded.observe(viewLifecycleOwner) {
            Toast.makeText(requireActivity(), getString(R.string.charts_loaded), Toast.LENGTH_SHORT).show()
        }
        adapter.clickedItem.observe(viewLifecycleOwner) {

        }
    }

    private fun info(msg: String) {
        Log.i(TAG, msg)
    }

    companion object {
        const val TAG = "CHARTS FRAGMENT"
    }

}