package com.mat.inspector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mat.inspector.databinding.FragmentParametersBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ParametersFragment : Fragment() {
    private lateinit var binding: FragmentParametersBinding
    private lateinit var valuesChangeWatcher: Job
    private val adapter: ParametersAdapter = ParametersAdapter()
    private val parametersViewModel: ParametersViewModel by viewModel()
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParametersBinding.inflate(inflater, container, false)
        binding.rvParameters.adapter = adapter
        binding.rvParameters.layoutManager = LinearLayoutManager(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configurationViewModel.configurationLiveData.observe(viewLifecycleOwner) { newConfig ->
            parametersViewModel.handleConfigurationChange(newConfig)
            if (this::valuesChangeWatcher.isInitialized) {
                if(valuesChangeWatcher.isActive) {
                    startValuesChangeWatcher(newConfig.serverAddress, newConfig.port)
                }
            }
        }
        adapter.clickedItem.observe(viewLifecycleOwner) { position ->

        }
        parametersViewModel.itemsToShow.observe(viewLifecycleOwner) { items ->
            adapter.setData(items)
        }
        binding.fabParametersAdd.setOnClickListener {
            NewParameterDialog().show(childFragmentManager, "np dialog")
        }
    }

    override fun onResume() {
        super.onResume()
        val currentConfig = configurationViewModel.configuration
        startValuesChangeWatcher(currentConfig.serverAddress, currentConfig.port)
    }

    override fun onPause() {
        stopValuesChangeWatcher()
        super.onPause()
    }

    private fun startValuesChangeWatcher(serverAddress: InetAddress, port: Int) {
        stopValuesChangeWatcher()

        info("starting values change watcher")
        valuesChangeWatcher = lifecycleScope.launch(Dispatchers.IO) {
            while(isActive) {
                info("watcher checking values...")
                parametersViewModel.reloadValues(serverAddress, port)
                delay(VALUES_CHANGE_WATCHER_INTERVAL_MS)
            }
        }
    }

    private fun stopValuesChangeWatcher() {
        if(this::valuesChangeWatcher.isInitialized) {
            if(valuesChangeWatcher.isActive) {
                info("stopping values change watcher")
                valuesChangeWatcher.cancel()
            }
        }
    }

    private fun info(msg: String) {
        Log.i(TAG, msg)
    }

    companion object {
        private const val TAG = "PARAMETERS FRAGMENT"
        private const val VALUES_CHANGE_WATCHER_INTERVAL_MS = 10_000L
    }

}