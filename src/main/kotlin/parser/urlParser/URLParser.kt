package parser.urlParser

import interfaces.IURLParser
import parser.ParserUtils

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<String> {
        val urls = parserUtils.parseValues(document, URLPatterns.A_TAG, URLPatterns.A_TAG_GROUP_INDEX)
        return urls.filter { url -> !URLPatterns.UNSUPPORTED_FILETYPES.matches(url) }
    }

    override fun getMainURL(document: String): String {
        return parserUtils.parseSingleValue(document, URLPatterns.MAIN_URL, URLPatterns.MAIN_URL_GROUP_INDEX)!!
    }
}