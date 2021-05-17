package com.mat.inspector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mat.inspector.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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

    private inner class PagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = slidingFragments.size

        override fun createFragment(position: Int) = slidingFragments[position].second.invoke()
    }
}