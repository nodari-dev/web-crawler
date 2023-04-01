import dao.Regex.Values.A_TAG
import dao.Regex.Values.GROUP_INDEX
import expetions.ParseHTMLException
import java.io.IOException
import java.lang.Exception
import java.net.URL

class BreathFirstSearch(startVertex: Vertex) {
    private val startVertex: Vertex = startVertex
    private var number = 0

    fun traverse(){
        val queue: MutableList<Vertex> = mutableListOf()
        val storage: MutableList<Int> = mutableListOf()

        queue.add(startVertex)

        while(queue.isNotEmpty()){
            val current: Vertex? = queue.removeFirstOrNull()
            if (current != null) {

                if(!current.isVisited() && !storage.contains(current.hashCode())){
                    storage.add(current.hashCode())
                    current.setVisited()
                    number += 1

                    println("$number Current " + current.getData())

                    val html = parseHtml(current)
                    if (html != null) {
                          A_TAG.findAll(html.toString()).forEach{ match ->
                            val childUrl = match.groups[GROUP_INDEX]!!.value
                              val hashCodeUrl = childUrl.hashCode()
                              if(!storage.contains(hashCodeUrl)) {
                                  storage.add(hashCodeUrl)
                                  current.setNeighbor(Vertex(childUrl))
                                  number += 1
                                  println("$number Child $childUrl")

                              }

                        }
                    }

                    queue.addAll(current.getNeighbors())
                }
            }
        }
    }

    private fun parseHtml(url: Vertex): String? {
        return try{
            URL(url.getData()).readText()
        } catch (e: IOException){
            println("Could not parse document: $e")
            null
        }
    }

}