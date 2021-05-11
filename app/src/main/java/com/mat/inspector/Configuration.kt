package com.mat.inspector

import android.content.Context
import com.beust.klaxon.Klaxon
import java.io.File

data class Configuration private constructor(
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

    fun load(context: Context): Configuration? {
        val configFilePath = "${context.filesDir}/${CONFIGURATION_FILE_NAME}"
        val configFile = File(configFilePath)
        val config: Configuration?
        val klaxon = Klaxon()
        if (configFile.exists()) {
            config = klaxon.parse<Configuration>(configFile)
        } else {
            configFile.createNewFile()
            config = Configuration()
            val jsonConfig = klaxon.toJsonString(config)
            configFile.writeText(jsonConfig)
        }
        return config
    }

    companion object {
        private const val CONFIGURATION_FILE_NAME = "config.json"
    }
}