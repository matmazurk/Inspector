package com.mat.inspector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class ConfigurationAdapter(private val config: Configuration) : RecyclerView.Adapter<ConfigurationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_configuration_item, parent, false)
        return ViewHolder(view)
    }

    /*
        [0.. readables.. n][n+1.. writables.. k][k+1.. charts p]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val readables = config.readables.size
        val writables = config.writables.size
        when(position) {
            in 0..readables -> {
                val configItem = config.readables[position]
                holder.apply {
                    name.text = configItem.name
                    address.text = configItem.address.toString()
                    type.text = configItem.type.toString()
                }
            }
            in readables + 1..writables -> {
                val shiftedPosition = position - readables - 1
                val configItem = config.writables[shiftedPosition]
                holder.apply {
                    name.text = configItem.name
                    address.text = configItem.address.toString()
                    type.text = configItem.type.toString()
                    details.text = "[${configItem.min};${configItem.max}]"
                }
            }
            in writables + readables + 1..itemCount -> {
                val shiftedPosition = position - readables - writables - 1
                val configItem = config.charts[shiftedPosition]
                holder.apply {
                    name.text = configItem.name
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return config.charts.size + config.readables.size + config.writables.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: MaterialTextView = view.findViewById(R.id.mtv_name)
        val address: MaterialTextView = view.findViewById(R.id.mtv_address)
        val type: MaterialTextView = view.findViewById(R.id.mtv_type)
        val details: MaterialTextView = view.findViewById(R.id.mtv_details)
    }

}