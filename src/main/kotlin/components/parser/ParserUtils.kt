package components.parser

import components.parser.GlobalPatterns.NESTED_TAGS
import core.interfaces.components.IParserUtils
import java.util.regex.Pattern

class ParserUtils : IParserUtils {

    override fun parseValues(
        html: String,
        extractionPattern: Pattern,
        groupIndex: Int
    ): List<String> {
        val values = mutableListOf<String>()

        val matcher = extractionPattern.matcher(html)

        while (matcher.find()) {
            val clearText = removeNestedTags(matcher.group(groupIndex))
            values.add(clearText.trim())
        }
        return values
    }

    override fun parseSingleValue(html: String, pattern: Pattern, groupIndex: Int): String? {
        val matcher = pattern.matcher(html)
        if (matcher.find()) {
            val value = matcher.group(groupIndex)
            return if (value.isNotEmpty()) {
                removeNestedTags(value.trim())
            } else {
                null
            }
        }
        return null
    }

    private fun removeNestedTags(text: String): String{
        val matcher = NESTED_TAGS.matcher(text)
        return matcher.replaceAll("")
    }

    override fun transformToFormattedURLs(list: List<String>): List<core.dto.HashedURLPair> {
        return list.map { element -> core.dto.HashedURLPair(element) }
    }

    override fun isExisting(html: String, pattern: Pattern): Boolean {
        val matcher = pattern.matcher(html.lowercase())
        return matcher.find()
    }
}