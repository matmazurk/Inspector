package com.mat.inspector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class ChartsAdapter : RecyclerView.Adapter<ChartsAdapter.ViewHolder>() {

    private val _clickedItem: MutableLiveData<ChartsViewModel.ChartWithData> = MutableLiveData()
    val clickedItem: LiveData<ChartsViewModel.ChartWithData> = _clickedItem
    private var data: List<ChartsViewModel.ChartWithData> = emptyList()

    fun setData(data: List<ChartsViewModel.ChartWithData>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_charts_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.chart.name
        holder.name.setOnClickListener {
            _clickedItem.postValue(item)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: MaterialTextView = view.findViewById(R.id.mtv_rv_charts_name)
    }
}