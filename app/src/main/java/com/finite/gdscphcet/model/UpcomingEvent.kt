package com.finite.gdscphcet.model

data class UpcomingEvent (
    val image: String = "",
    val title: String = "",
    val type: String = "",
    val url: String = "",
    val date: String = "",
    val tags: List<String> = listOf(),
    val description: String = ""
)