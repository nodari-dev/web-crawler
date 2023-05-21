import fetcher.Fetcher
import frontier.QueuesUtils
import parser.Parser
import services.DBConnector
import urlStorage.URLHashStorage

data class Counter(var value: Int)

class SingleHostCrawl(
    private val startPage: Page,
) {
    private val parser = Parser()
    private val fetcher = Fetcher()
    private val queuesUtils = QueuesUtils()
    private val dbConnector = DBConnector().init()

    private val urlHashDataStore = URLHashStorage
    private val counter = Counter(0)

    fun start() {
        val queue: MutableList<Page> = mutableListOf()
        queue.add(startPage)

        while (queue.isNotEmpty()) {
            val page: Page? = queue.removeFirstOrNull()
            if (page != null) {

                val hash = page.url.hashCode()

                if (isPageValid(page, hash)) {
                    urlHashDataStore.add(hash, page.url)
                    page.visited = true
                    counter.value += 1

                    println("Thread: ${Thread.currentThread().id} ${counter.value} Current ${page.url}")

                    val html = fetcher.getHTML(page)

                    if (html != null) {
                        val childLinks = parser.getChildLinks(html)
                        if (childLinks.isNotEmpty()) {
                            processChildUrls(html, page, counter)
                        }
                    }
                    queue.addAll(page.neighbors)
                }
            }
        }
    }

    private fun isPageValid(page: Page, hash: Int): Boolean {
        return !page.visited && !urlHashDataStore.includes(hash)
    }

    private fun processChildUrls(html: String, page: Page, counter: Counter) {
        parser.getChildLinks(html).forEach { childPage ->
            if (dbConnector != null) {
                queuesUtils.executeFrontQMutation(dbConnector, 1, listOf(childPage.url))
            }
            val hashCodeUrl = childPage.hashCode()
            if (!urlHashDataStore.includes(hashCodeUrl)) {
                page.neighbors.add(Page(childPage.url))
                counter.value += 1
                print("${counter.value} ${childPage.url}")
            }
            if (counter.value == 1000) return
        }
    }
}