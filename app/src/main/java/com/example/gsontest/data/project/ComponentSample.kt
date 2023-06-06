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


fun testComponent(context: Context) {
//    val components = arrayOf(
//        HeaderComponent(
//            header = Header("This is title", 1)
//        ),
//        ButtonComponent(
//            button = Button("Click me", "https://www.google.com")
//        )
//    )
//
//    val json = Gson().toJson(components)
//    Log.d("bbbb", "json: $json")
    val json = LocalResourceUtil.loadJSONFromAsset(context, "componentSample.json")

    val deserializer = ComponentDeserializer("type")
//    deserializer.registerBarnType("button", ButtonComponent::class.java)
//    deserializer.registerBarnType("header", HeaderComponent::class.java)
    val gson = GsonBuilder().registerTypeAdapter(Component::class.java, deserializer).create()

    val json2 =
        gson.fromJson<List<Component>>(json, object : TypeToken<List<Component?>?>() {}.type)
    Log.d("bbbb", "json2: $json2")
    for (component in json2) {
        when (component) {
            is HeaderComponent -> {
                Log.d(
                    "bbbb",
                    "headerText: ${component.header.headerText}, level: ${component.header.level}"
                )
            }

            is ButtonComponent -> {
                Log.d(
                    "bbbb",
                    "buttonText: ${component.button.buttonText}, deeplink: ${component.button.deeplink}"
                )
            }
        }
    }
}

internal class ComponentDeserializer(var componentTypeElementName: String) :
    JsonDeserializer<Component?> {
    var gson: Gson
    var componentTypeRegistry: MutableMap<String?, Class<out Component>>

    init {
        gson = Gson()
        componentTypeRegistry = HashMap() // Java 7 required for this syntax.
    }

    fun registerBarnType(barnTypeName: String?, componentType: Class<out Component>) {
        componentTypeRegistry[barnTypeName] = componentType
    }

    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Component {
        val componentObject = json.asJsonObject
        var component: Component
        when (componentObject.entrySet().firstOrNull()?.key) {
            "header" -> {
                component = HeaderComponent(
                    header = Gson().fromJson(
                        componentObject.entrySet().firstOrNull()!!.value,
                        Header::class.java
                    )
                )
            }

            "button" -> {
                component = ButtonComponent(
                    button = Gson().fromJson(
                        componentObject.entrySet().firstOrNull()!!.value,
                        Button::class.java
                    )
                )
            }

            else -> throw JsonParseException(
                "Unknown key: ${
                    componentObject.entrySet().firstOrNull()?.key
                }"
            )
        }
        return component
    }
}

sealed class Component

class HeaderComponent(val header: Header) : Component()
class ButtonComponent(val button: Button) : Component()

class Header(val headerText: String, val level: Int)
class Button(val buttonText: String, val deeplink: String)