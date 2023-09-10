package parser.urlparser

import dto.HashedURLPair
import interfaces.IURLParser
import parser.ParserUtils
import parser.urlparser.URLPatterns.A_TAG
import parser.urlparser.URLPatterns.A_TAG_GROUP_INDEX
import parser.urlparser.URLPatterns.HOST_WITH_PROTOCOL
import parser.urlparser.URLPatterns.HOST_WITH_PROTOCOL_GROUP_INDEX
import parser.urlparser.URLPatterns.UNSUPPORTED_FILETYPES

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<HashedURLPair> {
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