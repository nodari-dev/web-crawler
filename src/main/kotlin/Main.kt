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
}