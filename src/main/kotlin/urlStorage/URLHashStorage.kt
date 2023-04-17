package urlStorage

import exceptions.URLHashStorageQueryException
import services.DBConnector
import java.sql.SQLException

object URLHashStorage {

    // For local testing without db you can use storage
    // private val storage: MutableList<Int> = mutableListOf()

    private val connection = DBConnector().init()
    private val mutex = Object()

    // developer is too lazy to create new name for a method
    fun add(urlHash: Int, url: String){
        synchronized(mutex){
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
    }

    // same as above
    fun includes(urlHash: Int): Boolean{
        synchronized(mutex){
            if(connection != null){
                return try {
                    val query = connection.prepareStatement(("SELECT id, \"value\" FROM visited WHERE id=$urlHash;"))
                    query.executeQuery().next()
                }
                catch (e: URLHashStorageQueryException){
                    false
                    throw e
                }
            } else{
                println("No connection, this dumb dev will fix it")
                return false
            }
        }
    }
}