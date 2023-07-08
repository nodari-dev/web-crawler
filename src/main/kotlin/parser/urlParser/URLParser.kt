package parser.urlParser

import interfaces.IURLParser
import parser.ParserUtils

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<String> {
        val urls = parserUtils.parseValues(document, URLPatterns.A_TAG, URLPatterns.A_TAG_GROUP_INDEX)
        return urls.filterNot { url -> URLPatterns.UNSUPPORTED_FILETYPES.matches(url)}
    }

    override fun getHostWithProtocol(document: String): String {
        return parserUtils.parseSingleValue(document, URLPatterns.HOST_WITH_PROTOCOL, URLPatterns.HOST_WITH_PROTOCOL_GROUP_INDEX)!!
    }
}