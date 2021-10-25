package com.mat.inspector

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mat.inspector.databinding.FragmentChartDetailsBinding
import kotlin.random.Random


class ChartPlotFragment : Fragment() {

    @Suppress("MemberVisibilityCanBePrivate")
    val args: ChartPlotFragmentArgs by navArgs()
    private lateinit var binding: FragmentChartDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartDetailsBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val callback = object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(ChartPlotFragmentDirections.actionChartDetailsFragmentToConnectionFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback);
        loadChart(args.chart)
        binding.tvChartTitle.text = args.chart.name
    }

    private fun getEntries(xs: List<Double>, ys: List<Double>, samples: Int): List<Entry> {
        val spaceBetweenSamples: Int = xs.size / samples
        return List(samples) {
            Entry(xs[it * spaceBetweenSamples].toFloat(), ys[it * spaceBetweenSamples].toFloat())
        }
    }

    private fun getDataSet(xs: List<Double>, ys: List<Double>, samples: Int, title: String): LineDataSet {
        return LineDataSet(getEntries(xs, ys, samples), title).apply {
            val c = getRandomColor()
            setCircleColor(c)
            setDrawCircleHole(false)
            color = c
            circleRadius = 1.5f
        }
    }

    private fun loadChart(chart: Chart) {
        val lineData = LineData()
        val samplesPerOnePlot = chart.samples / chart.plots.size
        chart.plots.forEach { (title, id) ->
            val data = Connector.getData()[id]
            val startingTime = Connector.getData()[63][0]
            val timeShifted = Connector.getData()[63].map {
                (it - startingTime) * 1000
            }
            lineData.addDataSet(getDataSet(timeShifted, data, samplesPerOnePlot, title))
        }
        binding.lineChart.apply {
            axisLeft.apply {
                textSize = 15f
                setDrawAxisLine(true)
                setDrawGridLines(true)
                setDrawZeroLine(true)
            }
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textSize = 15f
                textColor = Color.BLACK
                setDrawAxisLine(true)
                setDrawGridLines(false)
            }
            axisRight.isEnabled = false
            description.isEnabled = false
            setScaleEnabled(false)
            data = lineData
        }.invalidate()
    }

    private fun getRandomColor(): Int {
        return Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
    }

}