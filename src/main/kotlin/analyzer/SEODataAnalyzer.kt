package analyzer

import dto.SEOContent
import interfaces.IDataAnalyzer
import parser.seoparser.SEOParser

class SEODataAnalyzer : IDataAnalyzer {
    private val seoParser = SEOParser()
    private val keywordGenerator = KeywordGenerator()

    fun generateSEOData(html: String, url: String): SEOContent{
        val sentences = generateSentences(html)
        val keywords = keywordGenerator.generateKeywords(sentences)
        return SEOContent(getTitle(html), getDescription(html), url, keywords)
    }

    private fun getTitle(html: String): String?{
        val title = seoParser.getTitle(html)
        val ogTitle = seoParser.getOgTitle(html)

        return listOfNotNull(title, ogTitle).firstOrNull()
    }

    private fun getDescription(html: String): String?{
        val metaDescription = seoParser.getMetaDescription(html)
        val ogMetaDescription = seoParser.getOMetaOgDescription(html)

        return listOfNotNull(metaDescription, ogMetaDescription).firstOrNull()
    }

    private fun generateSentences(html: String): List<String>{
        val title = seoParser.getTitle(html)
        val ogTitle = seoParser.getOgTitle(html)
        val metaDescription = seoParser.getMetaDescription(html)
        val ogMetaDescription = seoParser.getOMetaOgDescription(html)
        val metaKeywords = seoParser.getMetaKeywords(html)
        val headings = seoParser.getHeadings(html)
        val paragraphs = seoParser.getParagraphs(html)
        val alts = seoParser.getImageAlts(html)

        val notNullContent = listOfNotNull(title, ogTitle, metaDescription, ogMetaDescription)

        return listOf(notNullContent, metaKeywords, headings, paragraphs, alts).flatten()
    }
}