package com.mat.inspector

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.net.InetAddress


class ConfigurationViewModel(
    private val fileHandler: FileHandler,
) : ViewModel() {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().create()
    private var configFileMD5: String? = null
    var configuration: Configuration = Configuration()
        private set
    private val _configurationLiveData: MutableLiveData<Configuration> = MutableLiveData()
    val configurationLiveData: LiveData<Configuration> get() = _configurationLiveData

    fun reloadConfiguration(context: Context) {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val currentMD5 = fileHandler.getFileMD5(configFilePath)
        if (configFileMD5 == currentMD5) {
            Log.i("config", "from cache")
            return
        }
        configFileMD5 = currentMD5
        val configFileContent = fileHandler.getFileContent(configFilePath)

        if (configFileContent != null) {
            val jsonReader = JsonReader(configFileContent.reader())
            changeConfiguration(gson.fromJson(jsonReader, Configuration::class.java))
            Log.i("config", "from file")
        } else {
            val jsonConfig = gson.toJson(configuration)
            Log.i("config before save", configuration.toString())
            fileHandler.createNewFile(configFilePath, jsonConfig)
        }
        return
    }

    fun updateAddress(context: Context, newAddress: String): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        changeConfiguration(configuration.copy(serverAddress = InetAddress.getByName(newAddress)))
        return fileHandler.createNewFile(configFilePath, gson.toJson(configuration))
    }

    fun updatePort(context: Context, newPort: Int): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        changeConfiguration(configuration.copy(port = newPort))
        return fileHandler.createNewFile(configFilePath, gson.toJson(configuration))
    }

    fun addParameter(context: Context, parameter: Configuration.Parameter): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val parameters = configuration.parameters.toMutableList()
        parameters.add(parameter)
        changeConfiguration(configuration.copy(parameters = parameters))
        return fileHandler.createNewFile(configFilePath, gson.toJson(configuration))
    }

    fun removeParameter(context: Context, param: Configuration.Parameter): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val parameters = configuration.parameters.toMutableList()
        parameters.remove(param)
        changeConfiguration(configuration.copy(parameters = parameters))
        return fileHandler.createNewFile(configFilePath, gson.toJson(configuration))
    }

    fun addChart(context: Context, chart: Chart): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val charts = configuration.charts.toMutableList()
        charts.add(chart)
        changeConfiguration(configuration.copy(charts = charts))
        return fileHandler.createNewFile(configFilePath, gson.toJson(configuration))
    }

    fun removeChart(context: Context, chart: Chart): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val charts = configuration.charts.toMutableList()
        charts.remove(chart)
        changeConfiguration(configuration.copy(charts = charts))
        return fileHandler.createNewFile(configFilePath, gson.toJson(configuration))
    }

    private fun changeConfiguration(newConfig: Configuration) {
        Log.i("configuration view model", "configuration changed")
        configuration = newConfig
        _configurationLiveData.postValue(configuration)
    }

    companion object {
        private const val CONFIGURATION_FILE_NAME = "config.json"
    }
}