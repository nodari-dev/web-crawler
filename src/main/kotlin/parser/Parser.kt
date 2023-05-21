package parser

import Page
import dto.Regex.Values.A_TAG
import dto.Regex.Values.GROUP_INDEX

class Parser {
    fun getChildLinks(html: String): MutableList<Page> {
        val childUrls = mutableListOf<Page>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(Page(match.groups[GROUP_INDEX]!!.value))
        }

        return childUrls
    }

    fun getAllText(html: String): String{
        return "SOME TEXT"
    }
}