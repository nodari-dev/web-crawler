package storage

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

import core.dto.URLInfo
import infrastructure.repository.interfaces.IVisitedURLsRepository

class VisitedURLsTest {
    private val visitedURLsRepositoryMock = mock(IVisitedURLsRepository::class.java)
    private val visitedURLs = VisitedURLs(visitedURLsRepositoryMock)

    @Test
    fun `update works correct`(){
        val urlInfo = URLInfo("url")
        visitedURLs.update(urlInfo)
        verify(visitedURLsRepositoryMock).update(urlInfo)
    }

    @Test
    fun `filterByNewOnly works correct`(){
        val urls = listOf(URLInfo("url1"), URLInfo("url1"), URLInfo("url2"))
        `when`(visitedURLsRepositoryMock.getOnlyNewURLs(urls)).thenReturn(listOf(URLInfo("url1"), URLInfo("url1")))

        val result = visitedURLs.filterByNewOnly(urls)
        assertEquals(listOf(URLInfo("url1")), result)
    }

    @Test
    fun `isValid works correct`(){
        val url = "url"
        `when`(visitedURLsRepositoryMock.isNew(url)).thenReturn(true)

        val result = visitedURLs.isValid(url)
        assertEquals(true, result)
    }
}