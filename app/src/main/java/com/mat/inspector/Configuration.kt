package com.mat.inspector

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type
import java.net.Inet4Address
import java.net.InetAddress

data class Configuration(
    val serverAddress: InetAddress = Inet4Address.getByName("192.168.0.56"),
    val port: Int = 17825,
    val parameters: List<Parameter> = emptyList(),
    val charts: List<Chart> = emptyList(),
) {
    enum class Type {
        UINT,
        DOUBLE
    }

    data class Parameter(
        val name: String,
        val address: Int,
        val type: Type,
        val min: Double?,
        val max: Double?,
    ) {
        fun isWritable() = min != null && max != null
    }

    data class Chart(
        val name: String,
        val id: Int
    )
}