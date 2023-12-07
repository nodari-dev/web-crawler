package infrastructure.repository

import core.dto.RobotsData
import core.dto.URLInfo
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.sql.Connection
import java.util.concurrent.locks.ReentrantLock

class RobotsRepositoryTest{
    private val connectionMock = Mockito.mock(Connection::class.java)
    private val mutexMock = Mockito.mock(ReentrantLock::class.java)
    private val robotsRepository = RobotsRepository(mutexMock, connectionMock)

    @Test
    fun `update works correct`(){
        val host = "host"
        val urlsInfo = listOf(URLInfo("/some-blocked-url"), URLInfo("/another-blocked-url"))
        robotsRepository.update(host, RobotsData(urlsInfo))

        verify(mutexMock).lock()
        verify(connectionMock).prepareStatement("INSERT OR REPLACE INTO hosts values (?, ?, ?)")
        verify(mutexMock).unlock()
    }

    @Test
    fun `get works correct`(){
        robotsRepository.get("host")

        verify(mutexMock).lock()
        verify(connectionMock).prepareStatement("SELECT banned, delay FROM hosts WHERE host = ?")
        verify(mutexMock).unlock()
    }
}