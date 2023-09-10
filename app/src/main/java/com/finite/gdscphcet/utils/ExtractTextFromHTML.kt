package com.finite.gdscphcet.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.TextNode
import java.util.regex.Pattern

object ExtractTextFromHTML {

    fun extract(input: String): String {
        val removedPandBrTags = removeExtraTags(input)
        val node = Jsoup.parse(removedPandBrTags)
        val text = StringBuilder()
        val pattern = Pattern.compile("\\s{2,}")
        var inBoldTag = false
        var inUnderlineTag = false
        var inItalicTag = false

        fun processNode(node: Element) {
            when (node.tagName()) {
                "p" -> {
                    if (text.isNotEmpty()) {
                        text.append("\n\n") // Add a newline after each <p> tag ends
                    }
                }
                "br" -> text.append("\n") // Add a newline after each <br> tag

                "b" -> inBoldTag = true
                "u" -> inUnderlineTag = true
                "i" -> inItalicTag = true
            }

            for (child in node.childNodes()) {
                when (child) {
                    is TextNode -> {
                        val cleanText = pattern.matcher(child.text()).replaceAll(" ")
                        val formattedText = StringBuilder()

                        if (inBoldTag) {
                            formattedText.append(" *${cleanText.trim()}* ")
                        } else if (inUnderlineTag) {
                            formattedText.append(" _${cleanText.trim()}_ ")
                        } else if (inItalicTag) {
                            formattedText.append(" *_${cleanText.trim()}_* ")
                        } else {
                            formattedText.append(cleanText.trim())
                        }

                        text.append(formattedText)
                    }
                    is Element -> processNode(child)
                }
            }

            when (node.tagName()) {
                "b" -> inBoldTag = false
                "u" -> inUnderlineTag = false
                "i" -> inItalicTag = false
            }
        }

        processNode(node)
        return text.toString().trim()
    }

    private fun removeExtraTags(input: String): String {
        // Define the pattern to match "<p><br></p>"
        val pattern = Regex("<p><br></p>")

        val pattern2 = Regex("<br><br>")
        // Use the replace function to remove all occurrences of the pattern
        return input.replace(pattern2, "<br>").replace(pattern, "")
    }
}