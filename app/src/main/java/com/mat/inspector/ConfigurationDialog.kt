package com.mat.inspector

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mat.inspector.databinding.DialogConfigurationBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConfigurationDialog : DialogFragment() {

    private lateinit var binding: DialogConfigurationBinding
    private val viewModel: ConfigurationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfigurationBinding.inflate(inflater, container, false)
        configureLayout()
        setButtonListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val context = requireActivity()
        val configuration = viewModel.getConfiguration(context)
        binding.rvConfiguration.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ConfigurationAdapter(configuration)
        }
        binding.tietIpAddr.setText(configuration.serverAddress.hostAddress)
    }

    private fun configureLayout() {
        binding.layoutConfiguration.minimumWidth = 800
    }

    private fun setButtonListeners() {
        binding.btAccept.setOnClickListener {
            viewModel.updateAddress(requireActivity(), binding.tietIpAddr.text.toString())
            dismiss()
        }
        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }
}