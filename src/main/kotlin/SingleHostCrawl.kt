import fetcher.Fetcher
import parser.Parser
import urlStorage.URLHashStorage

data class Counter(var value: Int)

class SingleHostCrawl(
    startPage: Page,
) {
    private val fetcher = Fetcher()

    private val urlHashDataStore = URLHashStorage()
    private val counter = Counter(0)

    private val queue: MutableList<Page> = mutableListOf(startPage)

    fun start() {
        while (queue.isNotEmpty()) {
            val page: Page? = queue.removeFirstOrNull()
            if (isPageValid(page)) {
                processUrl(page!!)
            }
        }
    }

    private fun isPageValid(page: Page?): Boolean {
        return page != null && !urlHashDataStore.storage.contains(page.hash)
    }

    private fun processUrl(page: Page) {
        urlHashDataStore.storage.add(page.hash)
        counter.value++

        println("Fetched: ${page.url}")
        println(page.hash)

        val html = fetcher.getPageContent(page.url)
        if (html != null) {
            page.html = html
            processChildUrls(page)
        }

        queue.addAll(page.neighbors)
    }

    private fun processChildUrls(page: Page) {
        val urls = Parser.getFilteredUrls(page.html!!)
        urls.forEach { url ->
            if (!urlHashDataStore.storage.contains(url.hashCode())) {
                page.neighbors.add(Page(url))
                counter.value++
                println("Found: $url")
            }
            if (counter.value == 1000) return
        }
    }
}