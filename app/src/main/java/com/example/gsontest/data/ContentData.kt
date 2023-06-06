package com.example.gsontest.data

class ContentData {
    val list: List<ComponentData> = emptyList()
}

sealed class ComponentData {
    data class ButtonComponentData(
        val name: String,
        val style: String,
        val deeplink: String
    ) : ComponentData()

    data class HeaderComponentData(
        val name: String,
        val date: String,
        val description: String
    ) : ComponentData()

    data class CardComponentData(
        val id: Int,
        val name: String,
        val isActivated: Boolean,
        val isHighlight: Boolean,
        val expire: String
    ) : ComponentData()
}