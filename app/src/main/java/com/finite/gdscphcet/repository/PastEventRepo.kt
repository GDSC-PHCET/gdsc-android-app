package com.finite.gdscphcet.repository

import com.finite.gdscphcet.utils.AgendaItems.getAgendaItems
import com.finite.scrapingpractise.model.Agenda
import com.finite.scrapingpractise.model.PastEvent
import com.finite.scrapingpractise.model.PastEventDetails
import com.finite.scrapingpractise.model.UpcomingEvent
import com.google.gson.Gson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser

object PastEventRepo {

    fun getPastEventsList(url: String): MutableList<PastEvent> {

        val doc = Jsoup.connect(url).ignoreContentType(true).get()
        val eventsList: MutableList<PastEvent> = mutableListOf()
        val eventElements = doc.select("ul.vertical-boxes.past-event-list > a")

        for (eventElement in eventElements) {
            val image = eventElement.selectFirst("img.event-image")!!.attr("src")
            val eventDate = eventElement.selectFirst("p.vertical-box--event-date")!!.text()
            val eventType = eventElement.selectFirst("p.event-page.vertical-box--event-type")!!.text()
            val eventUrl = "https://gdsc.community.dev" + eventElement.attr("href")

            val eventTitleElement = eventElement.selectFirst("p.event-page.vertical-box--event-title")
            val eventTitle = Parser.unescapeEntities(eventTitleElement?.html()?: "", false).split("<br>").firstOrNull()?.trim() ?: ""

            val event = PastEvent(eventTitle, eventType, image, eventDate, eventUrl)

            eventsList.add(event)
        }
        return eventsList
    }

    fun getPastEventDetails(url: String): PastEventDetails {

        val doc = Jsoup.connect(url).get()

        val title = Parser.unescapeEntities(doc.selectFirst("h1.event-title-heading span.font_banner2")?.text()?: "", false)
        val dateTime = doc.select("#react-event-header-address > h2 > div").text() ?: ""
        val shortDesc = doc.selectFirst("p.event-short-description-on-banner")?.text() ?: ""
        val longDesc = doc.selectFirst("div.description-container div.event-description")?.text() ?: ""

        // Extract the date and time
        val secondColumn = doc.selectFirst("div.second-column")
        val whenDate = secondColumn?.select("br")?.first()?.previousSibling()?.toString()?.trim() ?: ""
        val whenTime = secondColumn?.select("br")?.first()?.nextSibling()?.toString()?.trim() ?: ""

        val mode = doc.selectFirst("#react-event-audience-type-chip")?.attr("data-event-audience-type") ?: ""

        val scriptElement = doc.selectFirst("script#event_tags")
        val tags = mutableListOf<String>()

        scriptElement?.html()?.let { jsonContent ->
            // Extract the content between "[" and "]" to get the JSON array
            val startIndex = jsonContent.indexOf("[")
            val endIndex = jsonContent.indexOf("]")

            if (startIndex != -1 && endIndex != -1) {
                val jsonArrayContent = jsonContent.substring(startIndex + 1, endIndex)

                // Split the JSON array content by commas and trim spaces
                val tagStrings = jsonArrayContent.split(",").map { it.trim() }

                // Remove double quotes from tag strings
                val cleanedTags = tagStrings.map { it.replace("\"", "") }

                // Add the tags to the list
                tags.addAll(cleanedTags)
            }
        }

        // Extract agenda items
        val agenda = getAgendaItems(doc)


        // Create and return a PastEventDetails object
        return PastEventDetails(title, mode, dateTime, tags, shortDesc, longDesc, whenDate, whenTime, agenda)
    }
}