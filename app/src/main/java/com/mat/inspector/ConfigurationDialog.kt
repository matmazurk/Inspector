package com.mat.inspector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
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
        val configuration = viewModel.configuration
        binding.tietIpAddr.setText(configuration.serverAddress.hostAddress)
        binding.tietPort.setText(configuration.port.toString())
    }

    private fun configureLayout() {
        binding.layoutConfiguration.minimumWidth = 800
    }

    private fun setButtonListeners() {
        binding.btAccept.setOnClickListener {
            viewModel.updateAddress(requireActivity(), binding.tietIpAddr.text.toString())
            viewModel.updatePort(requireActivity(), binding.tietPort.text.toString().toInt())
            dismiss()
        }
        binding.btCancel.setOnClickListener {
            dismiss()
        }
    }
}