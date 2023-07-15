package interfaces

import dto.FormattedURL

interface IParserUtils {
    fun parseSingleValue(html: String, regex: Regex, groupIndex: Int): String?
    fun parseValues(html: String, regex: Regex, groupIndex: Int): List<String>
    fun transformToFormattedURLs(list: List<String>): List<FormattedURL>
}