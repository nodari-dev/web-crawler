package crawler.crawlerLogger

import crawler.counter.Counter
import interfaces.ICrawlerLogger

object CrawlerLogger: ICrawlerLogger {
    override fun fetched(counter: Counter, value: String) {
        println("#${counter.value} Processed: $value")
    }

    override fun found(counter: Counter, value: String) {
        println("#${counter.value} Found: $value")
    }
}