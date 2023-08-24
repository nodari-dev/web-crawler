package fetcher

import org.jsoup.Connection
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class FetcherTest {

    @Test
    fun `returns page html`() {
        val response = mock(Connection.Response::class.java)
        `when`(response.parse()).thenReturn(Jsoup.parse("<html><body>Content</body></html>"))
        `when`(response.toString()).thenReturn("HTML content")

        val jsoupConnect = mock(Jsoup::class.java)
        `when`(jsoupConnect.(anyString())).thenReturn(response)
    }

    fun `returns null and throws exception if fetching failed`(){

    }
}