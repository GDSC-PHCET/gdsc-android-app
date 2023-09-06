package com.finite.scrapingpractise.model

data class PastEventDetails(
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

