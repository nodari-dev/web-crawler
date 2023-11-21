package application.fetcher

import application.fetcher.Fetcher
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class FetcherTest {
    private val fetcher = Fetcher()

    @Test
    fun `returns page html`() {
        val url = "https://example.com"
        val document = fetcher.getPageHTML(url)
        Assertions.assertNotNull(document)
    }

    @Test
    fun `returns null if fetching failed`(){
        val url = "someStupidURL"
        val document = fetcher.getPageHTML(url)
        Assertions.assertEquals(null, document)
    }
}