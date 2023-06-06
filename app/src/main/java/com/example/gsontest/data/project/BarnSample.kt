package com.example.gsontest.data.project

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


fun testBarn() {
    val barns = arrayOf(Barn(), Barn())
    barns[0].type = "horse"
    barns[0].animal = Horse("ponyA", 5)
    barns[1].type = "cow"
    barns[1].animal = Cow("white")

    val json = Gson().toJson(barns)
    Log.d("bbbb", "barn: $json")
    // [{"type":"horse","animal":{}},{"type":"cow","animal":{}}]

    // [{"type":"horse","animal":{}},{"type":"cow","animal":{}}]
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