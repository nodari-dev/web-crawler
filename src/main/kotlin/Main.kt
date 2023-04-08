import com.zaxxer.hikari.HikariDataSource
import java.nio.ByteBuffer
import java.sql.DriverManager
import java.util.LinkedList
import java.util.Queue

fun main() {
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


    class FrontQ (priorityId: Int, queue: Queue<String>){
        val q = queue

        fun getList(): Queue<String>{
            return q
        }

        fun add(url: String){
            q.add(url)
        }

        fun remove(): String{
            return q.poll()
        }
    }

    class BackQ (host: String, queue: Queue<String>){
        val q = queue
        val h = host

        fun getHost(): String{
            return this.h
        }

        fun getList(): Queue<String>{
            return q
        }

        fun add(url: String){
            q.add(url)
        }

        fun remove(): String{
            return q.poll()
        }
    }

    val q1: FrontQ = FrontQ(1, LinkedList<String>(mutableListOf("https://host.com", "https://google.com", "https://google.com/test")))
    val q3: FrontQ = FrontQ(2,  LinkedList<String>(mutableListOf("https://google.com/123", "https://host.com/main", "https://host.com/test")))
    val q2: BackQ = BackQ("https://host.com", LinkedList<String>(mutableListOf()))

    val allFrontQ: MutableList<FrontQ> = mutableListOf(q1, q3)
    val allBackQ: MutableList<BackQ> = mutableListOf(q2)

//    val front: MutableList<Queue<String>> = mutableListOf(q1, q3)
//    val back: MutableList<Queue<String>> = mutableListOf(q2)

    allFrontQ.forEach{queue ->
        while (queue.getList().isNotEmpty()){
            val nextUrl = queue.remove()
//            println("Front: $front")
//            println("Back: $back")
            if(nextUrl.contains(allBackQ[0].getHost())){
                allBackQ[0].add(nextUrl)
            }
//            println("Front: $front")
            println(allBackQ[0].getList())
//            println(allBackQ[0].getHost())
        }
    }
}