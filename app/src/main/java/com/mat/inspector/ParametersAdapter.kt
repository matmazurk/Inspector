package com.mat.inspector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class ParametersAdapter : RecyclerView.Adapter<ParametersAdapter.ViewHolder>() {

    private var data: List<ParametersViewModel.ParameterWithValue> = emptyList()
    private val _clickedItem: MutableLiveData<Configuration.Parameter> = MutableLiveData()
    private val _clickedDelBt: MutableLiveData<Configuration.Parameter> = MutableLiveData()
    val clickedItem: LiveData<Configuration.Parameter> get() = _clickedItem
    val clickedDel: LiveData<Configuration.Parameter> get() = _clickedDelBt

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
        holder.paramName.text = parameter.name
        holder.paramType.text = if(parameter.isWritable()) "${parameter.type}(W)" else parameter.type.toString()
        holder.paramValue.text = if(parameter.type == Configuration.Type.UINT) value.toInt().toString() else value.toString()
        holder.layout.setOnClickListener {
            _clickedItem.postValue(parameter)
        }
        holder.bt.setOnClickListener {
            _clickedDelBt.postValue(parameter)
        }
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: ConstraintLayout = view.findViewById(R.id.rv_parameters_item_layout)
        val paramName: MaterialTextView = view.findViewById(R.id.rv_parameters_item_title)
        val paramType: MaterialTextView = view.findViewById(R.id.rv_parameters_item_type)
        val paramValue: MaterialTextView = view.findViewById(R.id.rv_parameters_item_value)
        val bt: MaterialButton = view.findViewById(R.id.bt_param_delete)
    }

}