package analyzer

import interfaces.IDataAnalyzer
import parser.seoParser.SEOParser

class DataAnalyzer: IDataAnalyzer {
    private val seoParser = SEOParser()

    override fun getPageStats(html: String): String{
        println("Title ${seoParser.getTitle(html)}")
        println("OG Title ${seoParser.getOgTitle(html)}")
        println("Meta description ${seoParser.getMetaDescription(html)}")
        println("OG description ${seoParser.getOMetaOgDescription(html)}")
        println("META keywords ${seoParser.getMetaKeywords(html)}")
        println("ALT ${seoParser.getImageAlts(html)}")
        return ""
    }
}