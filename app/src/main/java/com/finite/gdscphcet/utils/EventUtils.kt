package com.finite.gdscphcet.utils

import com.finite.gdscphcet.model.Agenda
import com.google.gson.Gson
import org.jsoup.nodes.Document

object EventUtils {

    fun getAgendaItems(doc: Document): List<Agenda> {
        val agenda = mutableListOf<Agenda>()

        var scriptContent = ""

        val agendaElements = doc.getElementsByTag("script")
        for(a in agendaElements) {
            if(a.data().contains("eventInfo")) {
                scriptContent = a.data()
            }
        }

        val eventInfoRegex = Regex("Globals.eventInfo\\s*=\\s*([^;]+);")

        val eventInfoMatch = eventInfoRegex.find(scriptContent)

        if (eventInfoMatch != null) {
            val eventInfoJson = eventInfoMatch.groupValues[1]

            // Use Gson to parse the JSON-like data into a map
            val gson = Gson()
            val eventInfoMap = gson.fromJson(eventInfoJson, Map::class.java)

            // Access the "agenda" field and convert it to a list of Agenda objects
            val agendaList = (eventInfoMap["agenda"] as? Map<String, Any>)?.let { agendaMap ->
                val daysList = agendaMap["days"] as? List<Map<String, Any>> ?: emptyList()

                daysList.flatMap { day ->
                    val dayTitle = day["title"] as? String ?: ""
                    val agendaItems = day["agenda"] as? List<Map<String, String>> ?: emptyList()

                    agendaItems.map { agendaItem ->
                        Agenda(
                            time = agendaItem["time"] ?: "",
                            activity = agendaItem["activity"] ?: "",
                            description = agendaItem["description"] ?: ""
                        )
                    }
                }
            } ?: emptyList()

            // Now, you have the agendaList containing Agenda objects
            agendaList.forEach { it ->
                agenda.add(it)
            }
        } else {
            // println("EventInfo data not found in the script content.")
        }

        return agenda
    }

    fun getEventBannerUrl(doc: Document): String {
        // Define a regular expression pattern to extract the background image URL
        val pattern = Regex("\\.responsive-event-banner\\s*\\{[^}]*background-image:\\s*url\\(([^)]+)\\);")

        // Find the URL using the regular expression
        val matchResult = pattern.find(doc.toString())

        return matchResult?.groupValues?.get(1) ?: ""
    }

    fun getEventDetailsLogoUrl(doc: Document): String {
        // Find the meta tag with property="og:image"
        val metaTag = doc.select("meta[property=og:image]").first()

        // Extract the content attribute (URL) from the meta tag
        val imageUrl = metaTag?.attr("content")

        return imageUrl ?: ""
    }
}