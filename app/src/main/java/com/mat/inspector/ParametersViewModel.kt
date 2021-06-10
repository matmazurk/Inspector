package com.mat.inspector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetAddress

class ParametersViewModel(
    private val repository: RemoteRepository
) : ViewModel() {

    private var parameters: List<Configuration.Parameter> = emptyList()
    private val _itemsToShow: MutableLiveData<List<ParameterWithValue>> = MutableLiveData()
    val itemsToShow: LiveData<List<ParameterWithValue>> get() = _itemsToShow

    fun handleConfigurationChange(config: Configuration) {
        if (config.parameters != parameters) {
            viewModelScope.launch(Dispatchers.IO) {
                val items = fetchValuesForParams(config.parameters, config.serverAddress, config.port)
                _itemsToShow.postValue(items)
                parameters = config.parameters
            }
        }
    }

    fun reloadValues(serverAddress: InetAddress, port: Int) = viewModelScope.launch(Dispatchers.IO) {
        _itemsToShow.postValue(fetchValuesForParams(parameters, serverAddress, port))
    }

    private fun fetchValuesForParams(
        params: List<Configuration.Parameter>,
        serverAddress: InetAddress,
        port: Int,
    ) : List<ParameterWithValue> {
        val items: MutableList<ParameterWithValue> = mutableListOf()
        params.forEach { parameter ->
            if(parameter.type == Configuration.Type.UINT) {
                val value = repository.readUnsignedParameter(serverAddress, port, parameter.address)
                items.add(ParameterWithValue(parameter, value.toDouble()))
            } else {
                val value = repository.readDoubleParameter(serverAddress, port, parameter.address)
                items.add(ParameterWithValue(parameter, value))
            }
        }
        return items
    }

    class ParameterWithValue(
        val parameter: Configuration.Parameter,
        val value: Double,
    )

}