package com.mat.inspector

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.mat.inspector.databinding.DialogNewChartBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import android.view.MotionEvent
import android.widget.ListAdapter
import android.widget.ListView


class NewChartDialog : DialogFragment() {

    private lateinit var binding: DialogNewChartBinding
    private val configurationViewModel: ConfigurationViewModel by sharedViewModel()
    private val plots: MutableMap<String, Int> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNewChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btAddPlot.setOnClickListener {
            binding.layoutNewPlot.visibility = View.VISIBLE
            binding.btAddPlot.visibility = View.GONE
        }
        binding.btAddNewPlot.setOnClickListener {
            val plotTitle = binding.tietPlotTitle.text.toString()
            val plotID = binding.tietPlotNr.text.toString().toIntOrNull()
            when {
                plotTitle.isEmpty() -> {
                    Toast.makeText(requireActivity(), getString(R.string.plot_title_empty), Toast.LENGTH_SHORT).show()
                }
                plotID == null -> {
                    Toast.makeText(requireActivity(), getString(R.string.wrong_plot_id), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    plots[plotTitle] = plotID
                    binding.tietPlotNr.text?.clear()
                    binding.tietPlotTitle.text?.clear()
                    reloadListView()
                    binding.layoutNewPlot.visibility = View.GONE
                    binding.btAddPlot.visibility = View.VISIBLE
                }
            }
        }
        binding.btChartAccept.setOnClickListener {
            val name = binding.tietChartName.text.toString()
            val samples = binding.tietChartSamples.text.toString().toIntOrNull()
            when {
                name.isEmpty() -> {
                    Toast.makeText(requireActivity(), getString(R.string.chart_title_empty), Toast.LENGTH_SHORT).show()
                }
                samples == null -> {
                    Toast.makeText(requireActivity(), getString(R.string.samples_cnt_empty), Toast.LENGTH_SHORT).show()
                }
                plots.isEmpty() -> {
                    Toast.makeText(requireActivity(), getString(R.string.empty_plots), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val chart = Chart(name, plots, samples)
                    configurationViewModel.addChart(requireActivity(), chart)
                    dismiss()
                }
            }
        }
        binding.btChartCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun justifyListViewHeightBasedOnChildren(listView: ListView) {
        val adapter: ListAdapter = listView.adapter ?: return
        val vg: ViewGroup = listView
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem: View = adapter.getView(i, null, vg)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        val par: ViewGroup.LayoutParams = listView.layoutParams
        par.height = totalHeight + listView.dividerHeight * (adapter.count - 1)
        listView.layoutParams = par
        listView.requestLayout()
    }

    private fun reloadListView() {
        val adapterData = plots.map {
            "${it.key}:${it.value}"
        }
        val adapter =
            ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, adapterData)
        binding.lvPlots.adapter = adapter
        justifyListViewHeightBasedOnChildren(binding.lvPlots)
        binding.lvPlots.visibility = View.VISIBLE
    }
}