package fetcher

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class FetcherTest {
    private val mockLogger = Mockito.mock(KotlinLogging.logger("Fetcher")::class.java)
    private val fetcher = Fetcher(mockLogger)

    @Test
    fun `returns page html`() {
        val url = "https://example.com"
        val document = fetcher.getPageHTML(url)
        verify(mockLogger).info("downloading $url")
        Assertions.assertNotNull(document)
    }

    @Test
    fun `returns null if fetching failed`(){
        val url = "someStupidURL"
        val document = fetcher.getPageHTML(url)
        verify(mockLogger).info("downloading $url")
        verify(mockLogger).error("Fetching of $url failed")
        Assertions.assertEquals(null, document)
    }
}