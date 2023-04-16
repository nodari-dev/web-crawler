package parser

import Node
import dto.Regex.Values.A_TAG
import dto.Regex.Values.GROUP_INDEX

class Parser {
    fun getAllChildLinks(html: String): MutableList<Node> {
        val childUrls = mutableListOf<Node>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(Node(match.groups[GROUP_INDEX]!!.value))
        }

        return childUrls
    }

    fun getAllText(html: String): String{
        return "SOME TEXT"
    }
}