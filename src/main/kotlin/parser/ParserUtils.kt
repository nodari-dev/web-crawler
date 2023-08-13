package parser

import dto.FormattedURL
import interfaces.IParserUtils
import java.util.regex.Pattern

class ParserUtils: IParserUtils {

    override fun parseValues(html: String, pattern: Pattern, groupIndex: Int): List<String>{
        val values = mutableListOf<String>()

        val matcher = pattern.matcher(html)
        while(matcher.find()){
            values.add(matcher.group(groupIndex))
        }

        return values
    }

    override fun parseSingleValue(html: String, pattern: Pattern, groupIndex: Int): String?{
        val matcher = pattern.matcher(html)
        if(matcher.find()){
            return matcher.group(groupIndex)
        }
        return null
    }

    override fun transformToFormattedURLs(list: List<String>): List<FormattedURL>{
        return list.map { element -> FormattedURL(element) }
    }
}