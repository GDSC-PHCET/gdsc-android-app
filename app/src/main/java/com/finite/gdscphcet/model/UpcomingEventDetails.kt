package com.finite.scrapingpractise.model

data class UpcomingEventDetails(
    val title: String = "",
    val mode: String = "",
    val dateTime: String = "",
    val tags: List<String> = listOf(),
    val shortDesc : String = "",
    val longDesc : String = "",
    val whenDate : String = "",
    val whenTime : String = "",
    val agenda : List<Agenda> = listOf(),
)

data class Agenda(
    val time : String = "",
    val activity : String = "",
    val description : String = ""
)
