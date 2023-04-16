import fetcher.Fetcher
import frontier.QueuesUtils
import parser.Parser
import services.DBConnector

class BreadthFirstSearch(
    private val startNode: Node,
    private val urlHashDataStore: UrlHashDataStore,
    private val fetcher: Fetcher
) {
    private val parser = Parser()
    private var number = 0

    private val queuesUtils = QueuesUtils()

    private val dbConnector = DBConnector().init()

    fun traverse() {
        val queue: MutableList<Node> = mutableListOf()
        queue.add(startNode)

        while (queue.isNotEmpty()) {
            val current: Node? = queue.removeFirstOrNull()
            if (current != null) {

                val hashCurrent = current.getUrl().hashCode()

                if (!current.isVisited() && !urlHashDataStore.includes(hashCurrent)) {
                    urlHashDataStore.add(hashCurrent, current.getUrl())
                    current.setVisited()
                    number += 1

                    println("Thread: ${Thread.currentThread().id} $number Current " + current.getUrl())

                    val html = fetcher.getHTML(current)

                    if (html != null) {
                        val childLinks = parser.getAllChildLinks(html)
                        if(childLinks.isNotEmpty()){
                            parser.getAllChildLinks(html).forEach { childVertex ->
                                if(dbConnector != null){
                                    queuesUtils.executeFrontQMutation(dbConnector, 1, listOf(childVertex.getUrl()))
                                }
                                val hashCodeUrl = childVertex.getUrl().hashCode()
                                if (!urlHashDataStore.includes(hashCodeUrl)) {
                                    current.setNeighbor(Node(childVertex.getUrl()))
                                    number += 1
                                    print("$number ")
                                    println(childVertex.getUrl())
                                }
                                if(number == 100) return
                            }
                        }
                    }
                    queue.addAll(current.getNeighbors())
                }
            }
        }
    }
}