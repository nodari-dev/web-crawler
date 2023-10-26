package components.parser.urlparser

import components.interfaces.IURLParser
import components.parser.ParserUtils
import components.parser.urlparser.URLPatterns.A_TAG
import components.parser.urlparser.URLPatterns.A_TAG_GROUP_INDEX
import components.parser.urlparser.URLPatterns.HOST_WITH_PROTOCOL
import components.parser.urlparser.URLPatterns.HOST_WITH_PROTOCOL_GROUP_INDEX
import components.parser.urlparser.URLPatterns.UNSUPPORTED_FILETYPES

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<core.dto.WebLink> {
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