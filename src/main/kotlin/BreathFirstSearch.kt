import dto.Regex.Values.A_TAG
import dto.Regex.Values.GROUP_INDEX
import java.io.IOException
import java.net.URL

class BreathFirstSearch(startVertex: Vertex) {
    private val startVertex: Vertex = startVertex
    private val fetcher = Fetcher()
    private val parser = Parser()
    private var number = 0

    private val urlHashStorage = UrlHashDataStore()

    fun traverse(){
        val queue: MutableList<Vertex> = mutableListOf()
        queue.add(startVertex)

        while(queue.isNotEmpty()){
            val current: Vertex? = queue.removeFirstOrNull()
            if (current != null) {

                val hashCurrent = current.hashCode()

                if(!current.isVisited() && !urlHashStorage.includes(hashCurrent)){
                    urlHashStorage.add(hashCurrent)
                    current.setVisited()
                    number += 1

                    println("$number Current " + current.getUrl())

                    val html = fetcher.getHTML(current)


                    if (html != null) {
                        parser.getAllChildLinks(html).forEach { childVertex ->
                            val hashCodeUrl = childVertex.getUrl().hashCode()
                            if (!urlHashStorage.includes(hashCodeUrl)) {
                                urlHashStorage.add(hashCodeUrl)
                                current.setNeighbor(Vertex(childVertex.getUrl()))
                                number += 1
                                print("$number ")
                                println(childVertex.getUrl())
                            }
//                              if(number == 30) return
                        }

                    }
                    queue.addAll(current.getNeighbors())
                }
            }
        }
    }
}