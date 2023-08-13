package parser.urlParser

import dto.FormattedURL
import interfaces.IURLParser
import parser.ParserUtils
import parser.urlParser.URLPatterns.A_TAG
import parser.urlParser.URLPatterns.A_TAG_GROUP_INDEX
import parser.urlParser.URLPatterns.HOST_WITH_PROTOCOL
import parser.urlParser.URLPatterns.HOST_WITH_PROTOCOL_GROUP_INDEX
import parser.urlParser.URLPatterns.UNSUPPORTED_FILETYPES

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<FormattedURL> {
        val urls = parserUtils.parseValues(document, A_TAG, A_TAG_GROUP_INDEX)
        val allowedURLs = getAllowedURLs(urls)
        return parserUtils.transformToFormattedURLs(allowedURLs)
    }

    private fun getAllowedURLs(urls: List<String>): List<String>{
        return  urls.filterNot { url -> UNSUPPORTED_FILETYPES.matcher(url).find()}
    }

    override fun getHostWithProtocol(document: String): String {
        return parserUtils.parseSingleValue(document, HOST_WITH_PROTOCOL, HOST_WITH_PROTOCOL_GROUP_INDEX)!!
    }
}