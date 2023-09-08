package com.finite.scrapingpractise.model

import com.finite.gdscphcet.model.Agenda

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
    val bannerUrl : String = "",
    val logoUrl : String = ""
)


