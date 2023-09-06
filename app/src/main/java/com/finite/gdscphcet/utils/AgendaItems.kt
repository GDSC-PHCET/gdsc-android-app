package com.finite.gdscphcet.utils

import com.finite.scrapingpractise.model.Agenda
import com.google.gson.Gson
import org.jsoup.nodes.Document

object AgendaItems {

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
}