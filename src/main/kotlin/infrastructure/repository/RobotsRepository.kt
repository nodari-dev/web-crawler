package infrastructure.repository

import core.dto.RobotsData
import core.dto.URLInfo
import infrastructure.repository.interfaces.IRobotsRepository
import java.sql.Connection
import java.sql.SQLException
import java.util.concurrent.locks.ReentrantLock

class RobotsRepository(
    private val mutex: ReentrantLock,
    private val connection: Connection?
): IRobotsRepository {

    override fun update(host: String, robotsData: RobotsData) {
        val sql = """INSERT OR REPLACE INTO hosts values (?, ?, ?)"""
        mutex.lock()
        try{
            connection?.prepareStatement(sql).use { preparedStatement ->
                preparedStatement?.setString(1, host)
                preparedStatement?.setString(2, robotsData.bannedURLs.map{urlInfo -> urlInfo.link }.joinToString(","))
                preparedStatement?.setInt(3, robotsData.crawlDelay.toInt())
                preparedStatement?.execute()
            }
        }
        catch (e: SQLException){
            println(e)
        }
        finally {
            mutex.unlock()
        }
    }

    override fun get(host: String): RobotsData? {
        val sql = """SELECT banned, delay FROM hosts WHERE host = ?"""
        mutex.lock()
        var robotsData: RobotsData? = null
        try{
            connection?.prepareStatement(sql)?.apply {
                setString(1, host)

                val result = executeQuery()
                val bannedURLs = result.getString("banned")
                val delay = result.getInt("delay")
                if(bannedURLs != null){
                    robotsData = RobotsData(
                        bannedURLs.split(",").map { url -> URLInfo(url) },
                        delay.toLong()
                    )
                }
            }
        } finally {
            mutex.unlock()
        }

        return robotsData
    }
}