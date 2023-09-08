package com.finite.gdscphcet

import com.finite.gdscphcet.repository.PastEventRepo
import com.finite.gdscphcet.repository.UpcomingEventRepo
import com.finite.gdscphcet.utils.EventUtils
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScrapingUnitTest {

    //private val url = "https://gdsc.community.dev/pillai-hoc-college-of-engineering-and-technology-navi-mumbai/"
    //private val url = "https://gdsc.community.dev/shri-shankaracharya-technical-campus-bhilai/"
//    private val url = "https://gdsc.community.dev/pillai-college-of-engineering-navi-mumbai/"
    //private val url = "https://gdsc.community.dev/pranveer-singh-institute-of-technology-kanpur/"
//    private val url = "https://gdsc.community.dev/dy-patil-college-of-engineering-pune/"

    private val url = "https://gdsc.community.dev/mit-academy-of-engineering-pune/"

    private val pastEventUrl = "https://gdsc.community.dev/events/details/developer-student-clubs-dy-patil-college-of-engineering-pune-presents-google-io-extended-watch-party-pathway-to-become-a-senior-developer-at-paytm/"
    private val upcomingEventUrl = "https://gdsc.community.dev/events/details/developer-student-clubs-dy-patil-college-of-engineering-pune-presents-google-cloud-study-jam-session-1/"

    @Test
    fun `GET Upcoming Events`() {
        val upcomingEventsList = UpcomingEventRepo.getUpcomingEventsList(url)

        assert(upcomingEventsList.isNotEmpty())

        for ((index, event) in upcomingEventsList.withIndex()) {
            println("Upcoming Event #${index + 1}")
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
            val pastEventsList =
                PastEventRepo.getPastEventsList(url)

            assert(pastEventsList.isNotEmpty())

            for ((index, event) in pastEventsList.withIndex()) {
                println("Past Event #${index + 1}")
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
            val pastEventDetails = PastEventRepo.getPastEventDetails(pastEventUrl)

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
            println("Banner URL: ${pastEventDetails.bannerUrl}")
            println("Logo URL: ${pastEventDetails.logoUrl}")

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
            val upcomingEventDetails =
                UpcomingEventRepo.getUpcomingEventDetails(upcomingEventUrl)

            assert(upcomingEventDetails.title.isNotEmpty())

            // Print all the details to the console
            println("Title: ${upcomingEventDetails.title}")
            println("Type: ${upcomingEventDetails.mode}")
            println("Date and Time: ${upcomingEventDetails.dateTime}")
            println("Tags: ${upcomingEventDetails.tags.joinToString(", ")}")
            println("Short Description: ${upcomingEventDetails.shortDesc}")
            println("Long Description: ${upcomingEventDetails.longDesc}")
            println("When Date: ${upcomingEventDetails.whenDate}")
            println("When Time: ${upcomingEventDetails.whenTime}")
            println("Banner URL: ${upcomingEventDetails.bannerUrl}")
            println("Logo URL: ${upcomingEventDetails.logoUrl}")


            // Println("Agenda: ${pastEventDetails.agenda}")

            // Print agenda items if available
            if (upcomingEventDetails.agenda.isNotEmpty()) {
                println("Agenda:")
                for (agendaItem in upcomingEventDetails.agenda) {
                    println("- Time: ${agendaItem.time}")
                    println("  Activity: ${agendaItem.activity}")
                    println("  Description: ${agendaItem.description}")
                }
            }
        }

    }

    @Test
    fun `Get HTML Desc`() {
        val doc = PastEventRepo.getDocument("https://gdsc.community.dev/events/details/developer-student-clubs-ramrao-adik-institute-of-technology-navi-mumbai-presents-cyber-security-workshop/")

        val longDesc = EventUtils.getEventDetailsLongDesc(doc)

        println(longDesc)
    }

//    @Test
//    fun `Working Test for Past Event Banner`() {
//        val doc =
//            PastEventRepo.getDocument("" +
//                    "https://gdsc.community.dev/events/details/developer-student-clubs-dy-patil-college-of-engineering-pune-presents-google-cloud-study-jam-session-1/"
//            )
//
//        // Define a regular expression pattern to extract the background image URL
//        val pattern = Regex("\\.responsive-event-banner\\s*\\{[^}]*background-image:\\s*url\\(([^)]+)\\);")
//
//        // Find the URL using the regular expression
//        val matchResult = pattern.find(doc.toString())
//
//        if (matchResult != null) {
//            val url = matchResult.groupValues[1]
//            println(url)
//        } else {
//            println("Background image URL not found in the .responsive-event-banner block.")
//        }
//    }
//
//    @Test
//    fun `GET Banner URL`() {
//
//        val doc = PastEventRepo.getDocument("https://gdsc.community.dev/events/details/developer-student-clubs-pillai-hoc-college-of-engineering-and-technology-navi-mumbai-presents-gdsc-wow-mumbai/")
//        val url = EventUtils.getEventBannerUrl(doc)
//        println(url)
//        assert(url.isNotEmpty())
//
//        val doc2 = PastEventRepo.getDocument("https://gdsc.community.dev/events/details/developer-student-clubs-mit-academy-of-engineering-pune-presents-google-tech-and-gdsc/")
//        val url2 = EventUtils.getEventBannerUrl(doc2)
//        println(url2)
//        assert(url2.isNotEmpty())
//    }

}