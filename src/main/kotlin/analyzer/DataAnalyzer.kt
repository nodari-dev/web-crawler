package analyzer

import interfaces.IDataAnalyzer
import parser.seoparser.SEOParser
import java.util.regex.Pattern


class DataAnalyzer : IDataAnalyzer {
    private val seoParser = SEOParser()

    fun test(html: String) {
//        println(seoParser.getTitle(html))
//        println(seoParser.getHeadings(html))
//        println("META ${seoParser.getMetaDescription(html)}")
//        println("OG META ${seoParser.getOMetaOgDescription(html)}")
//        println(seoParser.getParagraphs(html))

        val text = mutableListOf<String>()
        val title = seoParser.getTitle(html)
        val ogTitle = seoParser.getOgTitle(html)
        val metaKeywords = seoParser.getMetaKeywords(html)
        val metaDescription = seoParser.getMetaDescription(html)
        val ogMetaDescription = seoParser.getOMetaOgDescription(html)
        val headings = seoParser.getHeadings(html)
        val paragraphs = seoParser.getParagraphs(html)
        val alts = seoParser.getImageAlts(html)

        if (title != null) {
            text.add(title)
        }
        if (ogTitle != null) {
            text.add(ogTitle)
        }

        if (metaDescription != null) {
            text.add(metaDescription)
        }

        if (ogMetaDescription != null) {
            text.add(ogMetaDescription)
        }

        text.addAll(metaKeywords)
        text.addAll(headings)
        text.addAll(paragraphs)
        text.addAll(alts)

        getKeywords(text)
    }

    // Step 1: create a list of strings
    // Step 2: remove all numbers and no text characters
    // Step 3: remove common words by regex
    // Step 4: find common root words

    private fun getKeywords(text: List<String>) {
        val keywords = HashMap<String, Int>()

        val clearArray = text.map { removeCommonWords(it) }

        for (word in clearArray) {
            val allWords = word.split(" ")
            for (keyword in allWords) {
                if (keyword.length > 2) {
                    keywords[keyword] = keywords.getOrDefault(keyword, 0) + 1
                }
            }
        }

        val resultMap = keywords.entries.sortedByDescending { it.value }.associate { it.toPair() }
        val result = resultMap.filter { it.value > 1 }
        result.forEach { println(it) }
    }

    fun removeCommonWords(input: String): String {
        val commonWords = "a|an|and|the|of|to|in|is|are|will|it|that|this|for|with|on|as|by|at|from|but|or|not|they|we|you|he|she|I|me|him|her|my|your|his|its|our|their|them|any|all|every|many|much|few|little|some|other|another|one|two|three|first|second|last|more|most|least|about|over|under|through|before|after|during|while|since|until|because|although|if|unless|than|then|there|these|here|now|when|where|how|who|what|which|why|whom|whose|whether|while|between|among|like|within|without|under|over|beside|behind|through|across|after|before|near|far|outside|inside|along|around|above|below|upon|within|against|amid|among|amid|beyond|but|concerning|considering|down|despite|except|following|for|like|minus|onto|out|past|per|regarding|round|save|since|throughout|to|towards|under|underneath|until|unto|up|via|within|without|worth|і|та|із|від|до|у|на|за|під|над|перед|поза|через|при|об|в|по|з|із|зі|був|була|було|були|ваш|ваша|ваше|ваші|він|вона|воно|вони|все|всі|всю|всім|всіх|від|він|вона|воно|вони|вісім|вісімнадцять|два|двадцять|дванадцять|де|дев'ять|дев'ятнадцять|десять|для|до|добре|другий|дуже|є|ж|з|за|зараз|знову|й|його|її|ймовірно|коли|краще|куди|ласка|лише|майже|ми|ти|міг|може|можливо|мій|моя|моє|мої|моєму|моїм|моєї|моїй|моїми|моїх|вас|вами|на|навіть|навіщо|нам|нами|нас|наш|наша|наше|наші|не|небудь|немає|ні|нього|нічого|ніякого|об|один|одна|одне|одні|особливо|поза|про|раз|разу|рік|саме|своє|своєї|свої|своїм|своїми|своїх|себе|собі|та|так|такий|така|таке|такі|там|теж|тепер|тобі|тобою|того|тоді|той|тому|туди|хіба|хто|це|цей|ці|цього|цьому|цю|ця|час|часу|через|чи|чого|чому|численна|численне|численні|чого|чому|що|який|яка|яке|які|якої|якщо|яку|які|якій"
        val commonWordsRegex =
            "\\b(?:$commonWords)\\b"
        val pattern = Pattern.compile(commonWordsRegex)
        val matcher = pattern.matcher(input.lowercase())
        return matcher.replaceAll("")
    }
}