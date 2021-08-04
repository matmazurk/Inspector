package com.mat.inspector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mat.inspector.HomeFragment.Companion.CONNECTION_MONITORING_DELAY_MS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class ParametersViewModel(
    private val repository: RemoteRepository
) : ViewModel() {


    private var parameters: List<Configuration.Parameter> = emptyList()
    private val _itemsToShow: MutableLiveData<List<ParameterWithValue>> = MutableLiveData()
    val itemsToShow: LiveData<List<ParameterWithValue>> get() = _itemsToShow

    private val _connError: MutableLiveData<String> = MutableLiveData()
    val connError: LiveData<String> get() = _connError

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

    fun writeDouble(serverAddress: InetAddress, port: Int, offset: Int, value: Double) = viewModelScope.launch(Dispatchers.IO) {
        repository.writeDoubleParameter(serverAddress, port, offset, value)
    }

    fun writeUint(serverAddress: InetAddress, port: Int, offset: Int, value: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.writeUnsignedParameter(serverAddress, port, offset, value)
    }

    private fun fetchValuesForParams(
        params: List<Configuration.Parameter>,
        serverAddress: InetAddress,
        port: Int,
    ) : List<ParameterWithValue> {
        info("fetching values for params")
        val items: MutableList<ParameterWithValue> = mutableListOf()
        if(isReachable(serverAddress.hostAddress, port, CONNECTION_MONITORING_DELAY_MS.toInt())) {
            params.forEach { parameter ->
                if (parameter.type == Configuration.Type.UINT) {
                    val value =
                        repository.readUnsignedParameter(serverAddress, port, parameter.address)
                    items.add(ParameterWithValue(parameter, value.toDouble()))
                } else {
                    val value =
                        repository.readDoubleParameter(serverAddress, port, parameter.address)
                    items.add(ParameterWithValue(parameter, value))
                }
            }
        } else {
            info("network error")
            _connError.postValue("network error")
        }
        return items
    }

    private fun info(msg: String) {
        Log.i(TAG, msg)
    }

    private fun isReachable(addr: String, openPort: Int, timeOutMillis: Int): Boolean {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        return try {
            Socket().use { soc ->
                soc.connect(
                    InetSocketAddress(addr, openPort),
                    timeOutMillis
                )
            }
            true
        } catch (ex: IOException) {
            false
        }
    }

    class ParameterWithValue(
        val parameter: Configuration.Parameter,
        val value: Double,
    )

    companion object {
        private const val TAG = "PARAMETERS VIEW MODEL"
    }

}