package interfaces

import dto.HashedUrlPair
import java.util.regex.Pattern

interface IParserUtils {
    fun parseSingleValue(html: String, pattern: Pattern, groupIndex: Int): String?
    fun parseValues(html: String, removingPattern: Pattern, extractionPattern: Pattern, groupIndex: Int): List<String>
    fun transformToFormattedURLs(list: List<String>): List<HashedUrlPair>
}