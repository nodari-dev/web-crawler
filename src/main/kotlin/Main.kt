import com.zaxxer.hikari.HikariDataSource
import crawler.Crawler
import frontier.BackQueue
import frontier.FrontQueue
import frontier.FrontierQueue
import java.nio.ByteBuffer
import java.sql.DriverManager
import java.util.LinkedList
import java.util.Queue

fun main() {
    val crawler = Crawler().initialize(1, Thread())

//    val seedUrls: List<String> = listOf(
//        "https://ecospace.org.ua/",
//    ).distinct()
//
//    val v0 = Vertex(seedUrls[0])
//    val bfs = BreathFirstSearch(v0)
//
//    bfs.traverse()

//    val dataSource = HikariDataSource()
//    dataSource.jdbcUrl = "jdbc:postgresql://localhost:5432/test"
//    dataSource.username = "test"
//    dataSource.password = "test"
//
//    println(dataSource.maximumPoolSize)

//    val connection = DriverManager.getConnection(connectionUrl, "test", "test")

//    val connection = dataSource.connection
//
//    val SQL: String = ("INSERT INTO mytable(name) "
//            + "VALUES(?)")
//
//    val mutation = connection.prepareStatement(SQL)
//    mutation.setString(1, "123")
//    mutation.executeUpdate()
//

//    val query = connection.prepareStatement("SELECT * FROM mytable")
//    val result = query.executeQuery()
//
//    while(result.next()){
//        val name = result.getString("name")
//
//        println("$name")
//    }


//    if(seedUrls.isNotEmpty()){
//        val crawler = Crawler<>(seedUrls)
//    } else{
//        println("No seed urls provided")
//    }




//    val q1: FrontQueue = FrontQueue(1, LinkedList<String>(mutableListOf("https://host.com", "https://google.com", "https://google.com/test")))
//    val q3: FrontQueue = FrontQueue(2,  LinkedList<String>(mutableListOf("https://google.com/123", "https://host.com/main", "https://host.com/test")))
//    val q2: BackQueue = BackQueue("https://host.com", LinkedList<String>(mutableListOf()))
//
//    val allFrontQ: MutableList<FrontQueue> = mutableListOf(q1, q3)
//    val allBackQ: MutableList<BackQueue> = mutableListOf(q2)
//
//    allFrontQ.forEach{queue ->
//        while (queue.getUrls().isNotEmpty()){
//            val nextUrl = queue.remove()
//            if(nextUrl.contains(allBackQ[0].getHost())){
//                allBackQ[0].add(nextUrl)
//            }
//            println(allBackQ[0].getUrls())
//        }
//    }
}