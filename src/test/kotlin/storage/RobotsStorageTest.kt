package storage

import mu.KLogger
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

import core.dto.RobotsData
import core.dto.URLInfo
import infrastructure.repository.RobotsRepository

class RobotsStorageTest {
    private val robotsRepositoryMock = Mockito.mock(RobotsRepository::class.java)
    private val loggerMock = Mockito.mock(KLogger::class.java)
    private val robotsStorage = RobotsStorage(robotsRepositoryMock, loggerMock)

    @Test
    fun `get works correct`(){
        val expectedResult = RobotsData(listOf(URLInfo("url")), 10)
        `when`(robotsRepositoryMock.get("host")).thenReturn(expectedResult)

        val result = robotsStorage.get("host")
        assertEquals(expectedResult, result)
    }

    @Test
    fun `update works correct`(){
        val robotsData = RobotsData(listOf(URLInfo("url")), 10)
        robotsStorage.update("host", robotsData)
        verify(robotsRepositoryMock).update("host", robotsData)
    }
}