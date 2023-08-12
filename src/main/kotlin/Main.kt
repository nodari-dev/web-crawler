import communicationManager.CommunicationManager

fun main() {
    val communicationManager = CommunicationManager
    communicationManager.addStartingPointURLs(listOf("https://ecospace.org.ua"))
    communicationManager.start()

//    val pool = JedisPool("localhost", 6379)
//
//    pool.resource.use { jedis ->
//        jedis["foo"] = "bar"
//        println(jedis["foo"]) // prints bar
//    }

}
