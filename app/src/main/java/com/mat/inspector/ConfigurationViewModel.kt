package com.mat.inspector

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.net.InetAddress


class ConfigurationViewModel(
    private val fileHandler: FileHandler,
    private val gson: Gson,
) : ViewModel() {

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
            fileHandler.createNewFile(configFilePath, jsonConfig)
            Log.i("config", "empty")
        }
        return
    }

    fun updateAddress(context: Context, newAddress: String): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        changeConfiguration(configuration.copy(serverAddress = InetAddress.getByName(newAddress)))
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