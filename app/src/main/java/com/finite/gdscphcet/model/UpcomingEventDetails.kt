package com.finite.gdscphcet.model

import com.finite.gdscphcet.model.Agenda
import org.jsoup.nodes.Element

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


