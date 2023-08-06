import controller.Controller

fun main() {
    val controller = Controller
    controller.addSeed("https://ecospace.org.ua")
    controller.addSeed("https://magecloud.agency")
    controller.start()

//    val pool = JedisPool("localhost", 6379)
//
//    pool.resource.use { jedis ->
//        jedis["foo"] = "bar"
//        println(jedis["foo"]) // prints bar
//    }

}
