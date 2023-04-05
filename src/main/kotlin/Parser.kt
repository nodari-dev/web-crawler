import dto.Regex.Values.A_TAG
import dto.Regex.Values.GROUP_INDEX

class Parser {
    fun getAllChildLinks(html: String): MutableList<Vertex> {
        val childUrls = mutableListOf<Vertex>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(Vertex(match.groups[GROUP_INDEX]!!.value))
        }

        return childUrls
    }

    fun getAllText(html: String): String{
        return "SOME TEXT"
    }
}