package com.mat.inspector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mat.inspector.databinding.DialogNewChartBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class NewChartDialog : DialogFragment() {

    private lateinit var binding: DialogNewChartBinding
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNewChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btChartAccept.setOnClickListener {
            val name = binding.tietChartName.text.toString()
            val id = binding.tietChartNr.text.toString().toIntOrNull()
            if(name.isNotEmpty()) {
                if(id != null) {
                    val chart = Configuration.Chart(name, id)
                    configurationViewModel.addChart(requireActivity(), chart)
                    dismiss()
                } else {
                    Toast.makeText(requireActivity(), getString(R.string.wrong_id), Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), getString(R.string.chart_name_empty), Toast.LENGTH_SHORT).show()
            }

        }
        binding.btChartCancel.setOnClickListener {
            dismiss()
        }
    }
}