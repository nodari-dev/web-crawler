package parser

import dto.HashedUrlPair
import interfaces.IParserUtils
import java.util.regex.Pattern

class ParserUtils : IParserUtils {

    override fun parseValues(
        html: String,
        removingPattern: Pattern,
        extractionPattern: Pattern,
        groupIndex: Int
    ): List<String> {
        val values = mutableListOf<String>()

        val matcher = extractionPattern.matcher(html)

        while (matcher.find()) {
            val clearText = removeTextFromRemovingPattern(removingPattern, matcher.group(groupIndex))
            values.add(clearText.trim())
        }
        return values
    }

    private fun removeTextFromRemovingPattern(removingPattern: Pattern, fondText: String): String{
        val matcher = removingPattern.matcher(fondText)
        return matcher.replaceAll("")
    }

    override fun parseSingleValue(html: String, pattern: Pattern, groupIndex: Int): String? {
        val matcher = pattern.matcher(html)
        if (matcher.find()) {
            val value = matcher.group(groupIndex)
            return if (value.isNotEmpty()) {
                value.trim()
            } else {
                null
            }
        }
        return null
    }

    override fun transformToFormattedURLs(list: List<String>): List<HashedUrlPair> {
        return list.map { element -> HashedUrlPair(element) }
    }

    override fun isExisting(html: String, pattern: Pattern): Boolean {
        val matcher = pattern.matcher(html.lowercase())
        return matcher.find()
    }
}