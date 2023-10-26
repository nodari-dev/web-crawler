package components.htmlAnalyzer

import core.dto.SEOContent
import components.interfaces.ISEODataAnalyzer
import components.parser.seoparser.SEOParser

class HTMLAnalyzer : ISEODataAnalyzer {
    private val seoParser = SEOParser()
    private val keywordGenerator = KeywordGenerator()

    override fun generateSEOData(html: String, url: String): SEOContent?{
        val sentences = generateSentences(html)
        val keywords = keywordGenerator.generateKeywords(sentences)
        return processSEOData(SEOContent(prepareTitle(html), prepareDescription(html), url, keywords))
    }

    private fun processSEOData(seoContent: SEOContent): SEOContent? {
        return if (seoContent.keywords.isEmpty()) {
            null
        } else seoContent
    }

    private fun prepareTitle(html: String): String?{
        val title = seoParser.getTitle(html)
        val ogTitle = seoParser.getOgTitle(html)

        return listOfNotNull(title, ogTitle).firstOrNull()
    }

    private fun prepareDescription(html: String): String?{
        val metaDescription = seoParser.getMetaDescription(html)
        val ogMetaDescription = seoParser.getOgMetaOgDescription(html)

        return listOfNotNull(metaDescription, ogMetaDescription).firstOrNull()
    }

    private fun generateSentences(html: String): List<String>{
        val title = seoParser.getTitle(html)
        val ogTitle = seoParser.getOgTitle(html)
        val metaDescription = seoParser.getMetaDescription(html)
        val ogMetaDescription = seoParser.getOgMetaOgDescription(html)
        val metaKeywords = seoParser.getMetaKeywords(html)
        val headings = seoParser.getHeadings(html)
        val paragraphs = seoParser.getParagraphs(html)
        val alts = seoParser.getImageAlts(html)

        val notNullContent = listOfNotNull(title, ogTitle, metaDescription, ogMetaDescription)
        return listOf(notNullContent, metaKeywords, headings, paragraphs, alts).flatten()
    }
}