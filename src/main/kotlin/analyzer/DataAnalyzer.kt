package analyzer

import interfaces.IDataAnalyzer
import parser.seoparser.SEOParser


class DataAnalyzer : IDataAnalyzer {
    private val seoParser = SEOParser()

    fun test(html: String){
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

        if(title != null){
            text.add(title)
        }
        if(ogTitle != null){
            text.add(ogTitle)
        }

        if(metaDescription != null){
            text.add(metaDescription)
        }

        if(ogMetaDescription != null){
            text.add(ogMetaDescription)
        }

        text.addAll(metaKeywords)
        text.addAll(headings)
        text.addAll(paragraphs)
        text.addAll(alts)

        println(text)

        getKeywords(text)
    }


    private fun getKeywords(text: List<String>){
        val keywords = HashMap<String, Int>()

        for(word in text){
            val allWords = word.split(" ")
            for(keyword in allWords){
                val lowerKeyword = keyword.lowercase()
                if(lowerKeyword.length > 2){
                    keywords[lowerKeyword] = keywords.getOrDefault(lowerKeyword, 0) + 1
                }
            }
        }

        val resultMap = keywords.entries.sortedByDescending { it.value }.associate { it.toPair() }
        val result = resultMap.filter { it.value > 1 }
        result.forEach{ println(it) }
    }
}