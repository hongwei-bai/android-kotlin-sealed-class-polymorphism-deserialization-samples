package com.example.gsontest.data.project

import android.content.Context
import android.util.Log
import com.example.gsontest.util.LocalResourceUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


fun testBarn(context: Context) {
//    val barns = arrayOf(Barn(), Barn())
//    barns[0].type = "horse"
//    barns[0].animal = Horse("ponyA", 5)
//    barns[1].type = "cow"
//    barns[1].animal = Cow("white")
//
//    val json = Gson().toJson(barns)
//    Log.d("bbbb", "barn: $json")
    // [{"type":"horse","animal":{}},{"type":"cow","animal":{}}]

    // [{"type":"horse","animal":{}},{"type":"cow","animal":{}}]
    val json = LocalResourceUtil.loadJSONFromAsset(context, "barnSample.json")

    val deserializer = BarnDeserializer("type")
    deserializer.registerBarnType("horse", Horse::class.java)
    deserializer.registerBarnType("cow", Cow::class.java)
    val gson = GsonBuilder().registerTypeAdapter(Barn::class.java, deserializer).create()

    val barns2 = gson.fromJson<List<Barn>>(json, object : TypeToken<List<Barn?>?>() {}.type)
    Log.d("bbbb", "barns2: $barns2")
    for (barn in barns2) {
        println(barn.animal!!.javaClass)
        when (barn.type) {
            "horse" -> {
                val horse = barn.animal as Horse
                Log.d("bbbb", "horse name: ${horse.name}, age: ${horse.age}")
            }

            "cow" -> {
                val cow = barn.animal as Cow
                Log.d("bbbb", "cow milk color: ${cow.milkColor}")
            }
        }
    }
}

internal class BarnDeserializer(var barnTypeElementName: String) :
    JsonDeserializer<Barn?> {
    var gson: Gson
    var barnTypeRegistry: MutableMap<String?, Class<out Animal>>

    init {
        gson = Gson()
        barnTypeRegistry = HashMap() // Java 7 required for this syntax.
    }

    fun registerBarnType(barnTypeName: String?, animalType: Class<out Animal>) {
        barnTypeRegistry[barnTypeName] = animalType
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Barn {
        val barnObject = json.asJsonObject
        Log.d("bbbb", "For jsonObj: $barnObject")
        barnObject.entrySet().forEach {
            Log.d("bbbb", "entrySet: key: ${it.key}, value: ${it.value}")
        }
        Log.d("bbbb", "======================")
//        val hasKeyNamedType = barnObject.has("type")
//        val hasKeyNamedAnimal = barnObject.has("animal")
//        val hasKeyNamedGood = barnObject.has("good")
//        Log.d("bbbb", "hasKeyNamedType: $hasKeyNamedType")
//        Log.d("bbbb", "hasKeyNamedAnimal: $hasKeyNamedAnimal")
//        Log.d("bbbb", "hasKeyNamedGood: $hasKeyNamedGood")

        val animalTypeElement = barnObject[barnTypeElementName]
        val barn = Barn()
        barn.type = animalTypeElement.asString
        val animalType = barnTypeRegistry[barn.type]!!
        barn.animal = gson.fromJson(barnObject["animal"], animalType)
        return barn
    }
}

internal class Barn {
    var type: String? = null
    var animal: Animal? = null
}

sealed class Animal
class Horse(val name: String, val age: Int) : Animal()
class Cow(val milkColor: String) : Animal()