import communicationManager.CommunicationManager


fun main() {
    val controller = CommunicationManager
    controller.addSeed("https://ecospace.org.ua")
    controller.start()

//    val pool = JedisPool("localhost", 6379)
//
//    pool.resource.use { jedis ->
//        jedis["foo"] = "bar"
//        println(jedis["foo"]) // prints bar
//    }

}
