package frontier

import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException


class QueuesUtils {

    fun executeFrontQMutation(connection: Connection, id: Int, urls: List<String>){
        val urlsString = urls.joinToString(",")
        try{
            val mutation = connection.prepareStatement(("INSERT INTO frontq(id,urls) VALUES(?,?)"))
            mutation.setInt(1, id)
            mutation.setString(2, urlsString)
            mutation.executeUpdate()
            mutation.close()
        } catch (e: SQLException){
            throw e
        }
    }

    fun executeBackQMutation(connection: Connection, id: Int, url: String){
        val mutation = connection.prepareStatement(("INSERT INTO backq(id,urls) VALUES($id, $url)"))
        mutation.executeUpdate()
    }

    fun getFrontQueues(connection: Connection): ResultSet? {
        val hash = "https://ecospace.org.ua".hashCode()
        val query = connection.prepareStatement(("SELECT id, \"urls\" FROM frontq WHERE id=$hash;"))
        return query.executeQuery()
//        val query = connection.prepareStatement("SELECT * FROM frontq")
//        return query.executeQuery()
    }

}