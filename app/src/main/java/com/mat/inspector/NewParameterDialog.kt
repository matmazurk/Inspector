package com.mat.inspector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mat.inspector.databinding.DialogNewParameterBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewParameterDialog : DialogFragment() {

    private lateinit var binding: DialogNewParameterBinding
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNewParameterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.checkBoxWritable.setOnCheckedChangeListener { _, checked ->
            binding.layoutNpMinmax.visibility = if(checked) View.VISIBLE else View.GONE
        }
        binding.btNpAccept.setOnClickListener {
            val newParameter = Configuration.Parameter(
                name = binding.tietNpName.text.toString(),
                address = binding.tietNpAddr.text.toString().toInt(),
                type = if(binding.spinner.selectedItem.toString() == Configuration.Type.UINT.toString())
                        Configuration.Type.UINT
                    else
                        Configuration.Type.DOUBLE,
                min = if(binding.checkBoxWritable.isChecked) binding.tietNpMin.text.toString().toDouble() else null,
                max = if(binding.checkBoxWritable.isChecked) binding.tietNpMax.text.toString().toDouble() else null
            )
            configurationViewModel.addParameter(requireActivity(), newParameter)
            dismiss()
        }
        binding.btNpCancel.setOnClickListener {
            dismiss()
        }
    }

}