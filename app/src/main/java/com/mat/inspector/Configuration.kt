package com.mat.inspector

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress

data class Configuration private constructor(
    val serverAddress: InetAddress = Inet4Address.getByName("192.168.0.1"),
    val readables: List<Readable> = emptyList(),
    val writables: List<Writable> = emptyList(),
    val charts: List<Chart> = emptyList(),
) {
    enum class Type {
        UINT,
        DOUBLE
    }

    data class Readable(
        val name: String,
        val address: Int,
        val type: Type,
    )

    data class Writable(
        val name: String,
        val address: Int,
        val type: Type,
        val min: Double,
        val max: Double,
    )

    data class Chart(
        val name: String,
        val data: List<Pair<Int, String>>
    )

    companion object {
        private const val CONFIGURATION_FILE_NAME = "config.json"

        fun load(context: Context): Configuration? {

            val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
            val configFile = File(configFilePath)
            val gson = Gson()
            val config: Configuration?
            if (configFile.exists()) {
                val jsonReader = JsonReader(configFile.reader())
                config = gson.fromJson(jsonReader, Configuration::class.java)
            } else {
                configFile.createNewFile()
                config = Configuration()
                val jsonConfig = gson.toJson(config)
                configFile.writeText(jsonConfig)
            }
            return config
        }
    }
}