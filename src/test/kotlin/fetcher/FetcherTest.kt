package fetcher

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FetcherTest {
    private val fetcher = Fetcher()

    @Test
    fun `returns page html`() {
        val url = "https://example.com"
        val document = fetcher.getPageContent(url)
        Assertions.assertNotNull(document)
    }

    @Test
    fun `returns null if fetching failed`(){
        val url = "someStupidURL"
        val document = fetcher.getPageContent(url)
        Assertions.assertEquals(null, document)
    }
}