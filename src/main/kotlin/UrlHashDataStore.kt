import services.DBConnector
import java.sql.Connection
import java.sql.SQLException

class

UrlHashDataStore {
//    private val storage: MutableList<Int> = mutableListOf()
    private val connection = DBConnector().init()

    fun add(urlHash: Int, url: String){
//        storage.add(urlHash)
        if(connection != null){
            try {
                val mutation = connection.prepareStatement(("INSERT INTO visited(id,value) VALUES(?,?)"))
                mutation.setInt(1, urlHash)
                mutation.setString(2, url)
                mutation.executeUpdate()
                mutation.close()
            } catch (e: SQLException) {
                throw e
            }
        }

    }

    fun includes(urlHash: Int): Boolean{
        if(connection != null){
            val query = connection.prepareStatement(("SELECT id, \"value\" FROM visited WHERE id=$urlHash;"))
            return (query.executeQuery().next())
        } else{
            println("No connection, this dumb dev will fix it")
            return false
        }

    }

    fun testGetAll(){
//        storage.forEach{item -> println(item)}
    }
}