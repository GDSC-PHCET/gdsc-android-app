package com.finite.gdscphcet.repository

import com.finite.gdscphcet.utils.EventUtils
import com.finite.gdscphcet.utils.EventUtils.getAgendaItems
import com.finite.scrapingpractise.model.UpcomingEventDetails
import com.finite.scrapingpractise.model.UpcomingEvent
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import org.jsoup.parser.Parser
import java.util.regex.Pattern

object UpcomingEventRepo {

    fun getUpcomingEventsList(url: String): MutableList<UpcomingEvent> {

        val doc = Jsoup.connect(url).timeout(10 * 1000).ignoreContentType(true).get()
        val eventsList: MutableList<UpcomingEvent> = mutableListOf()
        val eventElements = doc.select("ul.event-list > li.event")

        for (eventElement in eventElements) {
            val image = eventElement.selectFirst("img")?.attr("src") ?: ""
            val eventDate = eventElement.selectFirst("div.date strong")?.text() ?: ""
            val eventType = eventElement.selectFirst("div.date span")?.text() ?: ""
            val eventTitle = Parser.unescapeEntities(eventElement.selectFirst("h4")?.text() ?: "", false)
            //val eventUrl = ("https://gdsc.community.dev" + eventElement.selectFirst("a[href]")?.attr("href"))

            val href = eventElement.selectFirst("a[href]")?.attr("href")?: ""
            val eventUrl = if (href.startsWith("https://gdsc.community.dev")) {
                href // If it already starts with the prefix, use it as is
            } else {
                "https://gdsc.community.dev$href" // Otherwise, add the prefix
            }

            val description = eventElement.selectFirst("p.description")?.text() ?: ""

            // Extract tags
            val tagsElement = eventElement.selectFirst("div.react-tags-root")
            val tags = tagsElement?.attr("data-tags")?.split(",")?.map { it.trim() } ?: emptyList()

            val event = UpcomingEvent(image, eventTitle, eventType, eventUrl, eventDate, tags, description)
            eventsList.add(event)
        }
        return eventsList
    }

    fun getUpcomingEventDetails(url: String): UpcomingEventDetails {

        val doc = Jsoup.connect(url).timeout(10000).get()

        val title = Parser.unescapeEntities(doc.selectFirst("h1.event-title-heading span.font_banner2")?.text()?: "", false)
        val dateTime = doc.select("#react-event-header-address > h2 > div").text() ?: ""
        val shortDesc = doc.selectFirst("p.event-short-description-on-banner")?.text() ?: ""
        //val longDesc = doc.selectFirst("div.description-container div.event-description")?.text() ?: ""
        val longDesc = EventUtils.getEventDetailsLongDesc(doc).toString()
        val bannerUrl = EventUtils.getEventBannerUrl(doc)
        val logoUrl = EventUtils.getEventDetailsLogoUrl(doc)

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
        return UpcomingEventDetails(title, mode, dateTime, tags, shortDesc, longDesc, whenDate, whenTime, agenda, bannerUrl, logoUrl)
    }
}