package com.finite.gdscphcet

import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScrapingUnitTest {

    //private val url = "https://gdsc.community.dev/pillai-hoc-college-of-engineering-and-technology-navi-mumbai/"
    //private val url = "https://gdsc.community.dev/shri-shankaracharya-technical-campus-bhilai/"
    private val url = "https://gdsc.community.dev/pillai-college-of-engineering-navi-mumbai/"
    //private val url = "https://gdsc.community.dev/pranveer-singh-institute-of-technology-kanpur/"

    private val singleEventUrl = "https://gdsc.community.dev/events/details/developer-student-clubs-pillai-hoc-college-of-engineering-and-technology-navi-mumbai-presents-techignite-an-onboarding-to-gdsc-phcet/"

    @Test
    fun `GET Upcoming Events`() {
        val upcomingEventsList = UpcomingEventRepo.getUpcomingEventsList(url)

        assert(upcomingEventsList.isNotEmpty())

        for ((index, event) in upcomingEventsList.withIndex()) {
            println("Upcoming Event #${index+1}")
            println("Image: ${event.image}")
            println("Title: ${event.title}")
            println("Type: ${event.type}")
            println("URL: ${event.url}")
            println("Date: ${event.date}")
            println("Description: ${event.description}")
            println("Tags: ${event.tags.joinToString(", ")}")
            println()
        }
    }

    @Test
    fun `GET Past Events`() {
        runBlocking {
            val pastEventsList = PastEventRepo.getPastEventsList("https://gdsc.community.dev/pillai-hoc-college-of-engineering-and-technology-navi-mumbai/")

            assert(pastEventsList.isNotEmpty())

            for ((index, event) in pastEventsList.withIndex()) {
                println("Past Event #${index+1}")
                println("Image: ${event.image}")
                println("Title: ${event.title}")
                println("Type: ${event.type}")
                println("URL: ${event.url}")
                println("Date: ${event.date}")
                println()
            }
        }
    }

    @Test
    fun `GET Past Event Details`() {
        runBlocking {
            val pastEventDetails = PastEventRepo.getPastEventDetails(singleEventUrl)

            assert(pastEventDetails.title.isNotEmpty())

            // Print all the details to the console
            println("Title: ${pastEventDetails.title}")
            println("Type: ${pastEventDetails.mode}")
            println("Date and Time: ${pastEventDetails.dateTime}")
            println("Tags: ${pastEventDetails.tags.joinToString(", ")}")
            println("Short Description: ${pastEventDetails.shortDesc}")
            println("Long Description: ${pastEventDetails.longDesc}")
            println("When Date: ${pastEventDetails.whenDate}")
            println("When Time: ${pastEventDetails.whenTime}")

            // Println("Agenda: ${pastEventDetails.agenda}")

            // Print agenda items if available
            if (pastEventDetails.agenda.isNotEmpty()) {
                println("Agenda:")
                for (agendaItem in pastEventDetails.agenda) {
                    println("- Time: ${agendaItem.time}")
                    println("  Activity: ${agendaItem.activity}")
                    println("  Description: ${agendaItem.description}")
                }
            }
        }

    }

    @Test
    fun `GET Upcoming Event Details`() {
        runBlocking {
            val pastEventDetails = UpcomingEventRepo.getUpcomingEventDetails("https://gdsc.community.dev/events/details/developer-student-clubs-dy-patil-college-of-engineering-pune-presents-rotaract-dypiemr-x-gdsc-dypcoe-youth-seminar/")

            assert(pastEventDetails.title.isNotEmpty())

            // Print all the details to the console
            println("Title: ${pastEventDetails.title}")
            println("Type: ${pastEventDetails.mode}")
            println("Date and Time: ${pastEventDetails.dateTime}")
            println("Tags: ${pastEventDetails.tags.joinToString(", ")}")
            println("Short Description: ${pastEventDetails.shortDesc}")
            println("Long Description: ${pastEventDetails.longDesc}")
            println("When Date: ${pastEventDetails.whenDate}")
            println("When Time: ${pastEventDetails.whenTime}")

            // Println("Agenda: ${pastEventDetails.agenda}")

            // Print agenda items if available
            if (pastEventDetails.agenda.isNotEmpty()) {
                println("Agenda:")
                for (agendaItem in pastEventDetails.agenda) {
                    println("- Time: ${agendaItem.time}")
                    println("  Activity: ${agendaItem.activity}")
                    println("  Description: ${agendaItem.description}")
                }
            }
        }

    }
}