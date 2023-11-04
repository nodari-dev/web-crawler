package application.interfaces

import core.dto.URLData
import java.util.regex.Pattern

interface IParserUtils {
    fun parseSingleValue(html: String, pattern: Pattern, groupIndex: Int): String?
    fun parseValues(html: String, extractionPattern: Pattern, groupIndex: Int): List<String>
    fun transformToFormattedURLs(list: List<String>): List<URLData>
    fun isExisting(html: String, pattern: Pattern): Boolean
}