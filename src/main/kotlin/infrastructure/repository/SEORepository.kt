package infrastructure.repository

import core.dto.SEO
import infrastructure.gateways.interfaces.IDatabaseGateway
import infrastructure.repository.interfaces.ISEORepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.locks.ReentrantLock

class SEORepository(private val mutex: ReentrantLock, private val connection: Connection?): ISEORepository{

    override fun put(seo: SEO) {
        val sql = """INSERT OR REPLACE INTO seo values (?, ?, ?, ?)"""
//        mutex.lock()
        try{
            connection?.prepareStatement(sql).use { preparedStatement ->
                preparedStatement?.setString(1, seo.title)
                preparedStatement?.setString(2, seo.description)
                preparedStatement?.setString(3, seo.url)
                preparedStatement?.setString(4, seo.keywords)
                preparedStatement?.execute()
            }
        }
        catch (e: SQLException){
            println(e)
        }
        finally {
//            mutex.unlock()
        }
    }

    override fun search(requestedString: String): MutableList<SEO> {
        val sql = """SELECT * FROM seo WHERE title LIKE ? OR description LIKE ? OR keywords LIKE ?"""
//        mutex.lock()
        val results = mutableListOf<SEO>()
        try{
            requestedString.split(" ").forEach{
                connection?.prepareStatement(sql)?.apply {
                    val query = "%${it}%"
                    setString(1, query)
                    setString(2, query)
                    setString(3, query)

                    val result = executeQuery()
                    if(result != null){
                        while (result.next()) {
                            val seo = SEO(
                                result.getString("title"),
                                result.getString("description"),
                                result.getString("url"),
                                result.getString("keywords")
                            )
                            results.add(seo)
                        }
                    }
                    close()
                }
            }
        }
        catch (e: SQLException){
            println(e)
        }
        finally {
//            mutex.unlock()
        }
        return results
    }
}