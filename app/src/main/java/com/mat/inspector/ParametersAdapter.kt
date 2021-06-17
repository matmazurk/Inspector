package com.mat.inspector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class ParametersAdapter : RecyclerView.Adapter<ParametersAdapter.ViewHolder>() {

    private var data: List<ParametersViewModel.ParameterWithValue> = emptyList()
    private val _clickedItem: MutableLiveData<Int> = MutableLiveData()
    val clickedItem: LiveData<Int> get() = _clickedItem

    fun setData(data: List<ParametersViewModel.ParameterWithValue>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_parameters_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val parameter = data[position].parameter
        val value = data[position].value
        if (parameter.max != null && parameter.min != null) {
            //fade layout
            holder.paramName.textSize = 30f
            holder.layout.isClickable = true
        }
        holder.paramName.text = parameter.name
        holder.paramType.text = parameter.type.toString()
        holder.paramValue.text = if(parameter.type == Configuration.Type.UINT) value.toInt().toString() else value.toString()
        holder.layout.setOnClickListener {
            _clickedItem.postValue(position)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: LinearLayout = view.findViewById(R.id.rv_parameters_item_layout)
        val paramName: MaterialTextView = view.findViewById(R.id.rv_parameters_item_title)
        val paramType: MaterialTextView = view.findViewById(R.id.rv_parameters_item_type)
        val paramValue: MaterialTextView = view.findViewById(R.id.rv_parameters_item_value)
    }

}