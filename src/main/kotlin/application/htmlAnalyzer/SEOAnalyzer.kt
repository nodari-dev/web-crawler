package application.htmlAnalyzer

import core.dto.SEO
import application.interfaces.ISEOAnalyzer
import application.parser.seoparser.SEOParser
import core.dto.URLInfo

class SEOAnalyzer : ISEOAnalyzer {
    private val seoParser = SEOParser()
    private val keywordGenerator = KeywordGenerator()

    override fun generateSEO(html: String, urlInfo: URLInfo): SEO?{
        val sentences = generateSentences(html)
        val keywords = keywordGenerator.generateKeywords(sentences)
        return processSEOData(
            SEO(prepareTitle(html), prepareDescription(html), urlInfo.link, keywords)
            )
    }

    private fun processSEOData(seo: SEO): SEO? {
        return if (seo.keywords.isEmpty()) {
            null
        } else seo
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