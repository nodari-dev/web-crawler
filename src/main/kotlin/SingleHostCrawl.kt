import fetcher.Fetcher
import frontier.QueuesUtils
import parser.Parser
import services.DBConnector
import urlStorage.URLHashStorage

class SingleHostCrawl(
    private val startNode: Page,
) {
    private val parser = Parser()
    private val fetcher = Fetcher()
    private val queuesUtils = QueuesUtils()
    private val dbConnector = DBConnector().init()

    private val urlHashDataStore = URLHashStorage

    fun start() {
        var number = 0
        val queue: MutableList<Page> = mutableListOf()
        queue.add(startNode)

        while (queue.isNotEmpty()) {
            val current: Page? = queue.removeFirstOrNull()
            if (current != null) {

                val hashCurrent = current.url.hashCode()

                if (!current.visited && !urlHashDataStore.includes(hashCurrent)) {
                    urlHashDataStore.add(hashCurrent, current.url)
                    current.visited = true
                    number += 1

                    println("Thread: ${Thread.currentThread().id} $number Current ${current.url}")

                    val html = fetcher.getHTML(current)

                    if (html != null) {
                        val childLinks = parser.getAllChildLinks(html)
                        if (childLinks.isNotEmpty()) {
                            parser.getAllChildLinks(html).forEach { childPage ->
                                if (dbConnector != null) {
                                    queuesUtils.executeFrontQMutation(dbConnector, 1, listOf(childPage.url))
                                }
                                val hashCodeUrl = childPage.hashCode()
                                if (!urlHashDataStore.includes(hashCodeUrl)) {
                                    current.neighbors.add(Page(childPage.url))
                                    number += 1
                                    print("$number ${childPage.url}")
                                }
                                if (number == 1000) return
                            }
                        }
                    }
                    queue.addAll(current.neighbors)
                }
            }
        }
    }
}