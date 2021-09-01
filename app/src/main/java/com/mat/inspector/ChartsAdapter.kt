package com.mat.inspector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class ChartsAdapter : RecyclerView.Adapter<ChartsAdapter.ViewHolder>() {

    private val _clickedItem: MutableLiveData<Configuration.Chart> = MutableLiveData()
    val clickedItem: LiveData<Configuration.Chart> = _clickedItem
    private val _clickedDelBt: MutableLiveData<Configuration.Chart> = MutableLiveData()
    val clickedDel: LiveData<Configuration.Chart> get() = _clickedDelBt
    private var data: List<Configuration.Chart> = emptyList()

    fun setData(data: List<Configuration.Chart>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_charts_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.name
        holder.name.setOnClickListener {
            _clickedItem.postValue(item)
        }
        holder.bt.setOnClickListener {
            _clickedDelBt.postValue(item)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: MaterialTextView = view.findViewById(R.id.mtv_rv_charts_name)
        val bt: MaterialButton = view.findViewById(R.id.bt_chart_delete)
    }
}