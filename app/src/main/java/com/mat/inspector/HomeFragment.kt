package com.mat.inspector

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
        connectionAvailable.observe(viewLifecycleOwner) { available ->
            if(available) {
                binding.ivConnectionStatus.setImageResource(R.drawable.ic_green_light)
            } else {
                binding.ivConnectionStatus.setImageResource(R.drawable.ic_red_light)
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val config = configurationViewModel.getConfiguration(requireActivity())
        startConnectionMonitoring(config.serverAddress)
    }

    override fun onPause() {
        stopConnectionMonitoring()
        super.onPause()
    }

    private fun startConnectionMonitoring(address: InetAddress) {
        Log.i("home fragment", "creating connection monitoring job")
        connectionJob = lifecycleScope.launch(Dispatchers.IO) {
            while(isActive) {
                if(address.isReachable(1000)) {
                    connectionAvailable.postValue(true)
                } else {
                    connectionAvailable.postValue(false)
                }
                delay(1000)
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

}