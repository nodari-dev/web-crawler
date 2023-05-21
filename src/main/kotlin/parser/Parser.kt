package parser

import dto.Regex.Values.A_TAG
import dto.Regex.Values.GROUP_INDEX

class Parser {
    fun getUrls(html: String): MutableList<String> {
        val childUrls = mutableListOf<String>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(match.groups[GROUP_INDEX]!!.value)
        }

        return childUrls
    }

    fun getAllText(html: String): String{
        return "SOME TEXT"
    }
}