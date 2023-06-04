package interfaces

import crawler.counter.Counter

interface ICrawlerLogger {
    fun fetched(counter: Counter, value: String)
    fun found(counter: Counter, value: String)
}