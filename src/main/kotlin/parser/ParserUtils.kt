package parser

import dto.FormattedURL
import interfaces.IParserUtils

class ParserUtils: IParserUtils {

    override fun parseValues(html: String, regex: Regex, groupIndex: Int): List<String>{
        val values = mutableListOf<String>()

        regex.findAll(html).forEach { match ->
            values.add(match.groups[groupIndex]!!.value)
        }

        return values
    }

    override fun parseSingleValue(html: String, regex: Regex, groupIndex: Int): String?{
        return regex.find(html)?.groups?.get(groupIndex)?.value?.trim()
    }

    override fun transformToFormattedURLs(list: List<String>): List<FormattedURL>{
        return list.map { element -> FormattedURL(element) }
    }
}