package com.example.gsontest.data

import android.content.Context
import android.util.Log
import com.example.gsontest.data.project.testBarn
import com.example.gsontest.data.project.testComponent
import com.example.gsontest.util.LocalResourceUtil
import com.google.gson.GsonBuilder
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory


class ContentRepository {
    fun test(context: Context) {
        val jsonString = LocalResourceUtil.loadJSONFromAsset(context, "button.json")
        Log.d("bbbb", "jsonStr: $jsonString")

//        jsonString?.let {
//            solution4(jsonString)
//        }
        testBarn(context)
        testComponent(context)
    }
}

fun solution4(jsonString: String) {
//    val list = mutableListOf<ComponentData>()
//    val gb = GsonBuilder()
//    gb.registerTypeAdapter(ComponentData::class.java, CustomDeserializer())
//    val gson = gb.create()

    val adapter: RuntimeTypeAdapterFactory<ComponentData> = RuntimeTypeAdapterFactory
        .of(ComponentData::class.java)
        .registerSubtype(ComponentData.ButtonComponentData::class.java)
        .registerSubtype(ComponentData.HeaderComponentData::class.java)
        .registerSubtype(ComponentData.CardComponentData::class.java)

    val gsonBuilder = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(adapter)

    val gson = gsonBuilder.create()

    val newC1: ComponentData = gson.fromJson(jsonString, ComponentData::class.java)
    Log.d("bbbb", "newC1: $newC1")
}

fun solution3() {
    val adapter: RuntimeTypeAdapterFactory<ComponentData> = RuntimeTypeAdapterFactory
        .of(ComponentData::class.java)
        .registerSubtype(ComponentData.ButtonComponentData::class.java)
        .registerSubtype(ComponentData.HeaderComponentData::class.java)
        .registerSubtype(ComponentData.CardComponentData::class.java)

    val gsonBuilder = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapterFactory(adapter)

    val gson = gsonBuilder.create()
    //        val fromJson = gson.fromJson(jsonString, adapter)
//        Log.d("bbbb", "listObj: $fromJson")

}