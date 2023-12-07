package operators

import core.dto.SEO
import core.dto.SearchResult
import infrastructure.repository.interfaces.ISEORepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.test.assertEquals

class SearchOperatorTest {
    private val sqliteRepositoryMock = mock(ISEORepository::class.java)
    private val searchOperator = SearchOperator(sqliteRepositoryMock)

    @Test
    fun `sorts by score, ascending`(){
        val request = "weather forecast"
        val resultFromRepository = mutableListOf(
            SEO( "second", "description", "url", "weather,forecast,test,something"),
            SEO( "first", "description", "url", "test,something,weather,forecast"),
            SEO( "third", "description", "url", "weather,something,test"),
        )
        `when`(sqliteRepositoryMock.search(request)).thenReturn(resultFromRepository)

        val result = searchOperator.searchAndSortByPriority(request, 1, 2)
        assertEquals(resultFromRepository[1].title, result.results[0].title)
        assertEquals(resultFromRepository[0].title, result.results[1].title)

        val resultSecondPage = searchOperator.searchAndSortByPriority(request, 2, 2)
        assertEquals(resultFromRepository[2].title, resultSecondPage.results[0].title)
    }

    @Test
    fun `returns empty result if pagination is out of bounds`(){
        val request = "weather forecast"
        val resultFromRepository = mutableListOf(
            SEO( "second item in search", "description", "some-url", "forecast,something,test,weather")
        )
        `when`(sqliteRepositoryMock.search(request)).thenReturn(resultFromRepository)
        val expectedResult = SearchResult(emptyList(), 0)
        val result = searchOperator.searchAndSortByPriority(request, 2, 2)
        assertEquals(expectedResult, result)
    }
}