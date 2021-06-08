package com.mat.inspector

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress

data class Configuration(
    val serverAddress: InetAddress = Inet4Address.getByName("192.168.0.56"),
    val port: Int = 17825,
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
        val address: String,
        val type: Type,
    )

    data class Writable(
        val name: String,
        val address: String,
        val type: Type,
        val min: Double,
        val max: Double,
    )

    data class Chart(
        val name: String,
        val data: List<Pair<Int, String>>
    )
}