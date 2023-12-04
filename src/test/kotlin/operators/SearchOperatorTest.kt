package operators

import core.dto.SEO
import infrastructure.repository.interfaces.ISEORepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

class SearchOperatorTest() {
    private val sqliteRepositoryMock = mock(ISEORepository::class.java)
    private val searchOperator = SearchOperator(sqliteRepositoryMock)

    @Test
    fun `sorts by score, ascending`(){
        val request = "weather forecast"
        val resultFromRepository = mutableListOf(
            SEO( "second item in search", "description", "some-url", "forecast,something,weather,test"),
            SEO( "first item in search", "description", "some-url", "forecast,something,test,weather"),
            SEO( "third item in search", "description", "some-url", "weather,something,test"),
        )
        `when`(sqliteRepositoryMock.search(request)).thenReturn(resultFromRepository)

        val result = searchOperator.searchAndSortByPriority(request)
        assertEquals(resultFromRepository[1].title, result[0].title)
        assertEquals(resultFromRepository[0].title, result[1].title)
        assertEquals(resultFromRepository[2].title, result[2].title)
    }
}