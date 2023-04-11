import fetcher.Fetcher
import parser.Parser

class BreathFirstSearch(
    private val startVertex: Vertex,
    private val urlHashDataStore: UrlHashDataStore
) {
    private val fetcher = Fetcher()
    private val parser = Parser()
    private var number = 0


    fun traverse() {
        val queue: MutableList<Vertex> = mutableListOf()
        queue.add(startVertex)

        while (queue.isNotEmpty()) {
            val current: Vertex? = queue.removeFirstOrNull()
            if (current != null) {

                val hashCurrent = current.getUrl().hashCode()

                if (!current.isVisited() && !urlHashDataStore.includes(hashCurrent)) {
                    urlHashDataStore.add(hashCurrent, current.getUrl())
                    current.setVisited()
                    number += 1

                    println("${Thread.currentThread()} $number Current " + current.getUrl())

                    val html = fetcher.getHTML(current)

                    if (html != null) {
                        parser.getAllChildLinks(html).forEach { childVertex ->
                            val hashCodeUrl = childVertex.getUrl().hashCode()
                            if (!urlHashDataStore.includes(hashCodeUrl)) {
                                current.setNeighbor(Vertex(childVertex.getUrl()))
                                number += 1
                                print("$number ")
                                println(childVertex.getUrl())
                            }
                            if(number == 50) return
                        }
                    }
                    queue.addAll(current.getNeighbors())
                }
            }
        }
    }
}