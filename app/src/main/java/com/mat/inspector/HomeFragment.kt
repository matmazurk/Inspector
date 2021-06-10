package com.mat.inspector

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mat.inspector.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.net.InetAddress
import java.net.Socket

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var connectionJob: Job
    private lateinit var configurationChangeJob: Job
    private val connectionAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()
    private val slidingFragments: List<Pair<String, () -> Fragment>> = listOf(
//        getString() to { Fragment() },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btConfiguration.setOnClickListener {
            ConfigurationDialog().show(childFragmentManager, "configuration dialog")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        connectionAvailable.observe(viewLifecycleOwner) { available ->
            if(available) {
                binding.ivConnectionStatus.setImageResource(R.drawable.ic_green_light)
            } else {
                binding.ivConnectionStatus.setImageResource(R.drawable.ic_red_light)
            }
        }
        configurationViewModel.configurationLiveData.observe(viewLifecycleOwner) { newConfig ->
            if(this::connectionJob.isInitialized) {
                if(connectionJob.isActive) {
                    startConnectionMonitoring(newConfig.serverAddress)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val config = configurationViewModel.configuration
        startConnectionMonitoring(config.serverAddress)
        startConfigurationChangeMonitoring(requireActivity())
    }

    override fun onPause() {
        stopConnectionMonitoring()
        stopConfigurationChangesMonitoring()
        super.onPause()
    }

    private fun startConfigurationChangeMonitoring(context: Context) {
        stopConfigurationChangesMonitoring()
        Log.i("home fragment", "creating configuration change monitoring job")
        configurationChangeJob = lifecycleScope.launch(Dispatchers.IO) {
            while(isActive) {
                configurationViewModel.reloadConfiguration(context)
                delay(CONFIGURATION_CHANGE_MONITORING_DELAY_MS)
            }
        }
    }

    private fun stopConfigurationChangesMonitoring() {
        if(this::configurationChangeJob.isInitialized) {
            if(configurationChangeJob.isActive) {
                Log.i("home fragment", "killing configuration monitoring job")
                configurationChangeJob.cancel()
            }
        }
    }

    private fun startConnectionMonitoring(address: InetAddress) {
        stopConnectionMonitoring()
        Log.i("home fragment", "creating connection monitoring job")
        connectionJob = lifecycleScope.launch(Dispatchers.IO) {
            while(isActive) {
                if(address.isReachable(CONNECTION_MONITORING_DELAY_MS.toInt())) {
                    connectionAvailable.postValue(true)
                    delay(CONNECTION_MONITORING_DELAY_MS)
                } else {
                    connectionAvailable.postValue(false)
                }
            }
        }

    }

    private fun stopConnectionMonitoring() {
        if(this::connectionJob.isInitialized) {
            if(connectionJob.isActive) {
                Log.i("home fragment", "killing connection monitoring job")
                connectionJob.cancel()
            }
        }
    }

    private inner class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = slidingFragments.size

        override fun createFragment(position: Int) = slidingFragments[position].second.invoke()
    }

    companion object {
        const val CONNECTION_MONITORING_DELAY_MS=1000L
        const val CONFIGURATION_CHANGE_MONITORING_DELAY_MS=120_000L
    }

}