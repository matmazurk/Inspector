package com.mat.inspector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mat.inspector.databinding.FragmentWritablesBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WritablesFragment : Fragment() {

    private lateinit var binding: FragmentWritablesBinding
    private val writablesViewModel: WritablesViewModel by viewModel()
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWritablesBinding.inflate(inflater, container, false)
        return binding.root
    }

}