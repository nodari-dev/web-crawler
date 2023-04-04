import dto.Regex.Values.A_TAG
import dto.Regex.Values.GROUP_INDEX
import java.io.IOException
import java.net.URL

class BreathFirstSearch(startVertex: Vertex) {
    private val startVertex: Vertex = startVertex
    private var number = 0

    private val urlHashStorage = UrlHashDataStore()

    fun traverse(){
        val queue: MutableList<Vertex> = mutableListOf()


        queue.add(startVertex)

        while(queue.isNotEmpty()){
            val current: Vertex? = queue.removeFirstOrNull()
            if(number == 30){break}
            if (current != null) {

                val hashCurrent = current.hashCode()

                if(!current.isVisited() && !urlHashStorage.includes(hashCurrent)){
                    urlHashStorage.add(hashCurrent)
                    current.setVisited()
                    number += 1

                    println("$number Current " + current.getUrl())

                    val html = parseHtml(current)
                    if (html != null) {
                          A_TAG.findAll(html.toString()).forEach{ match ->
                            val childUrl = match.groups[GROUP_INDEX]!!.value
                              val hashCodeUrl = childUrl.hashCode()
                              if(!urlHashStorage.includes(hashCodeUrl)) {
                                  urlHashStorage.add(hashCodeUrl)
                                  current.setNeighbor(Vertex(childUrl))
                                  number += 1
                                  println("$number Child $childUrl")
                              }
                              if(number == 30) return
                        }
                    }
                    queue.addAll(current.getNeighbors())
                }
            }
        }
    }

    private fun parseHtml(url: Vertex): String? {
        return try{
            URL(url.getUrl()).readText()
        } catch (e: IOException){
            println("Could not parse document: $e")
            null
        }
    }

}