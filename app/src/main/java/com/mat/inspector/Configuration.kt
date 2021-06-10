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
        @JsonAdapter(EmptyStringAsNullTypeAdapter::class)
        val min: Double?,
        @JsonAdapter(EmptyStringAsNullTypeAdapter::class)
        val max: Double?,
    )

    data class Chart(
        val name: String,
        val data: List<Pair<Int, String>>
    )

    class EmptyStringAsNullTypeAdapter<T> private constructor() : JsonDeserializer<T> {
        override fun deserialize(
            jsonElement: JsonElement,
            type: java.lang.reflect.Type?,
            context: JsonDeserializationContext?
        ): T? {
            if (jsonElement.isJsonPrimitive) {
                val jsonPrimitive = jsonElement.asJsonPrimitive
                if (jsonPrimitive.isString && jsonPrimitive.asString.isEmpty()) {
                    return null
                }
            }
            return context?.deserialize(jsonElement, type)
        }
    }
}