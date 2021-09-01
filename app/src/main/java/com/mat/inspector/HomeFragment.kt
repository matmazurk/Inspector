package com.mat.inspector

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mat.inspector.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var connectionJob: Job
    private val connectionAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()
    private val slidingFragments: List<Pair<String, () -> Fragment>> = listOf(
        "Params" to { ParametersFragment() },
        "Charts" to { ChartsFragment() },
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        configurationViewModel.reloadConfiguration(requireActivity())
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btConfiguration.setOnClickListener {
            ConfigurationDialog().show(childFragmentManager, "configuration dialog")
        }
        binding.pager.adapter = PagerAdapter(this)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = slidingFragments[position].first
        }.attach()
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
                    startConnectionMonitoring(newConfig.serverAddress, newConfig.port)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val config = configurationViewModel.configuration
        startConnectionMonitoring(config.serverAddress, config.port)
    }

    override fun onPause() {
        stopConnectionMonitoring()
        super.onPause()
    }


    private fun startConnectionMonitoring(address: InetAddress, port: Int) {
        stopConnectionMonitoring()
        info("creating connection monitoring job")
        connectionJob = lifecycleScope.launch(Dispatchers.IO) {
            while(isActive) {
                if(isReachable(address.hostAddress, port,  CONNECTION_MONITORING_DELAY_MS.toInt())) {
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
                info("killing connection monitoring job")
                connectionJob.cancel()
            }
        }
    }

    private fun info(msg: String) {
        Log.i(TAG, msg)
    }

    private fun isReachable(addr: String, openPort: Int, timeOutMillis: Int): Boolean {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        return try {
            Socket().use { soc ->
                soc.connect(
                    InetSocketAddress(addr, openPort),
                    timeOutMillis
                )
            }
            true
        } catch (ex: IOException) {
            false
        }
    }

    private inner class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = slidingFragments.size

        override fun createFragment(position: Int) = slidingFragments[position].second.invoke()
    }

    companion object {
        const val TAG = "HOME FRAGMENT"
        const val CONNECTION_MONITORING_DELAY_MS=1000L
    }

}