import crawlersManager.CrawlersManager


fun main() {
    val manager = CrawlersManager()
    manager.addSeed("https://ecospace.org.ua")
    manager.startCrawling()

//    val pool = JedisPool("localhost", 6379)
//
//    pool.resource.use { jedis ->
//        jedis["foo"] = "bar"
//        println(jedis["foo"]) // prints bar
//    }

}
