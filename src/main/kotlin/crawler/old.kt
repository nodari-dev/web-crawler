//package crawler
//
//import dto.Page
//import fetcher.Fetcher
//import interfaces.ITerminalCrawler
//import mu.KotlinLogging
//import dataAnalyzer.parser.Parser
//
//class TerminalCrawler(
//    override val controller: CrawlerController, override val id: Int
//
//) : ITerminalCrawler {
//    private val logger = KotlinLogging.logger("TerminalCrawler")
//
//    override fun start() {
//        while (controller.canProceedCrawling()) {
//            val page: Page? = queue.removeFirstOrNull()
//            page?.let {
//                if (controller.canProcessURL(page.url)) {
//                    processPage(page)
//                }
//            }
//        }
//    }
//
//    private fun processPage(page: Page) {
//        controller.updateStorage(page.hash)
//        counter.value++
//        logger.info { "#${counter.value} Processed: ${page.url}" }
//        val html = Fetcher.getPageContent(page.url)
//        html?.let {
//            page.html = html
//            processChildURLs(page)
//        }
//
//        queue.addAll(page.neighbors)
//    }
//
//    private fun processChildURLs(page: Page) {
//        val urls = Parser.getURLs(page.html!!)
//        urls.forEach { url ->
//            if (controller.canProcessURL(url)) {
//                val neighbor = Page(url)
//                page.neighbors.add(neighbor)
//                logger.info { "Found: $url" }
//            }
//        }
//    }
//}