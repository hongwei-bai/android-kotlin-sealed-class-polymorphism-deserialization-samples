package com.example.gsontest.data

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.util.TreeMap


class CustomDeserializer : JsonDeserializer<List<ComponentData?>?> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement, typeOfT: Type?,
        context: JsonDeserializationContext
    ): List<ComponentData> {
        val list: MutableList<ComponentData> = ArrayList()
        val ja = json.asJsonArray
        for (je in ja) {
            val type = je.asJsonObject["isA"].asString
            val c = map[type]
                ?: throw RuntimeException("Unknow class: $type")
            list.add(context.deserialize<Any>(je, c) as ComponentData)
        }
        return list
    }

    companion object {
        private val map: MutableMap<String, Class<*>> = TreeMap()

        init {
            map["ComponentData"] = ComponentData::class.java
            map["ButtonComponentData"] = ComponentData.ButtonComponentData::class.java
            map["HeaderComponentData"] = ComponentData.HeaderComponentData::class.java
            map["CardComponentData"] = ComponentData.CardComponentData::class.java
        }
    }
}