package application.parser.urlparser

import application.interfaces.IURLParser
import application.parser.ParserUtils
import application.parser.urlparser.URLPatterns.A_TAG
import application.parser.urlparser.URLPatterns.A_TAG_GROUP_INDEX
import application.parser.urlparser.URLPatterns.HOSTNAME
import application.parser.urlparser.URLPatterns.HOSTNAME_GROUP_INDEX
import application.parser.urlparser.URLPatterns.UNSUPPORTED_FILETYPES
import core.dto.URLInfo

class URLParser: IURLParser {
    private val parserUtils = ParserUtils()

    override fun getURLs(document: String): List<URLInfo> {
        val urls = parserUtils.parseValues(document, A_TAG, A_TAG_GROUP_INDEX)
        val allowedURLs = getAllowedURLs(urls)
        return parserUtils.transformToFormattedURLs(allowedURLs)
    }

    private fun getAllowedURLs(urls: List<String>): List<String>{
        return urls.filterNot{ url -> UNSUPPORTED_FILETYPES.any{filetype -> url.endsWith(filetype)}}
    }

    override fun getHostname(document: String): String {
        return parserUtils.parseSingleValue(document, HOSTNAME, HOSTNAME_GROUP_INDEX)!!
    }
}