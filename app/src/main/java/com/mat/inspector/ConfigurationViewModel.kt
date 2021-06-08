package com.mat.inspector

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import java.net.Inet4Address
import java.net.InetAddress


class ConfigurationViewModel(
    private val fileHandler: FileHandler,
    private val gson: Gson,
) : ViewModel() {

    private var configFileMD5: String? = null
    private var config: Configuration = Configuration()

    fun getConfiguration(context: Context): Configuration {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val currentMD5 = fileHandler.getFileMD5(configFilePath)
        if (configFileMD5 == currentMD5) {
            Log.i("config", "from cache")
            return config
        }
        configFileMD5 = currentMD5
        val configFileContent = fileHandler.getFileContent(configFilePath)
        if (configFileContent != null) {
            val jsonReader = JsonReader(configFileContent.reader())
            config = gson.fromJson(jsonReader, Configuration::class.java)
            Log.i("config", "from file")
        } else {
            config = Configuration()
            val jsonConfig = gson.toJson(config)
            fileHandler.createNewFile(configFilePath, jsonConfig)
            Log.i("config", "empty")
        }
        return config
    }

    fun updateAddress(context: Context, newAddress: String): Boolean {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        config = config.copy(serverAddress = InetAddress.getByName(newAddress))
        return fileHandler.createNewFile(configFilePath, gson.toJson(config))
    }

    companion object {
        private const val CONFIGURATION_FILE_NAME = "config.json"
    }
}