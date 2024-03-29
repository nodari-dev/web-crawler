package application.parser

import java.util.regex.Pattern

import core.dto.URLInfo
import application.parser.GlobalPatterns.NESTED_TAGS
import application.interfaces.IParserUtils

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

    override fun toURLInfo(list: List<String>): List<URLInfo> {
        return list.map { element -> URLInfo(element) }
    }

    override fun isExisting(html: String, pattern: Pattern): Boolean {
        val matcher = pattern.matcher(html.lowercase())
        return matcher.find()
    }
}