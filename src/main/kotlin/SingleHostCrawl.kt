import fetcher.Fetcher
import parser.Parser
import URLHashStorage.URLHashStorage
import dto.Page

data class Counter(var value: Int)

class SingleHostCrawl(
    startPage: Page,
    private val maxNumberOfURLs: Int = 50,
) {
    private val fetcher = Fetcher

    private val uhs = URLHashStorage()
    private val counter = Counter(0)

    private val queue: MutableList<Page> = mutableListOf(startPage)

    fun start() {
        while (queue.isNotEmpty() && proceedCrawling()) {
            val page: Page? = queue.removeFirstOrNull()
            if (page != null && isURLValid(page.url)) {
                processPage(page)
            }
        }
    }

    private fun isURLValid(url: String): Boolean {
        return !urlHashDataStore.storage.contains(url.hashCode())
    }

    private fun processPage(page: Page) {
        urlHashDataStore.storage.add(page.hash)
        counter.value++

        println("#${counter.value} Fetched: ${page.url}")
        println(page.url.hashCode())

        val html = fetcher.getPageContent(page.url)
        if (html != null) {
            page.html = html
            processChildUrls(page)
        }

        queue.addAll(page.neighbors)
    }

    private fun processChildUrls(page: Page) {
        val urls = Parser.getFilteredURLs(page.html!!)
        urls.forEach { url ->
            if (!isURLValid(url)) {
                page.neighbors.add(Page(url))
                counter.value++
                println("#${counter.value} Found: $url")
            }
            if (!proceedCrawling()) return
        }
    }

    private fun proceedCrawling(): Boolean{
        return counter.value != maxNumberOfURLs
    }
}