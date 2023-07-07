package parser.urlParser

import dto.HostWithProtocol
import interfaces.IURLParser
import parser.ParserUtils

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<String> {
        val urls = parserUtils.parseValues(document, URLPatterns.A_TAG, URLPatterns.A_TAG_GROUP_INDEX)
        return urls.filter { url -> !URLPatterns.UNSUPPORTED_FILETYPES.matches(url) }
    }

    override fun getHostWithProtocol(document: String): HostWithProtocol {
        return HostWithProtocol(parserUtils.parseSingleValue(document, URLPatterns.HOST_WITH_PROTOCOL, URLPatterns.HOST_WITH_PROTOCOL_GROUP_INDEX)!!)
    }
}