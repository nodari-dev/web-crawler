package application.interfaces

import java.util.regex.Pattern
import core.dto.URLInfo

interface IParserUtils {
    fun parseSingleValue(html: String, pattern: Pattern, groupIndex: Int): String?
    fun parseValues(html: String, extractionPattern: Pattern, groupIndex: Int): List<String>
    fun toURLInfo(list: List<String>): List<URLInfo>
    fun isExisting(html: String, pattern: Pattern): Boolean
}