package application.crawler

import core.dto.URLData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class URLPackerTest {
    private val urlPacker = URLPacker()

    @Test
    fun `packs urls related to specific host`(){
        val urls = listOf(
            URLData("https://host-one.com"),
            URLData("https://host-one.com/child"),
            URLData("https://host-two.com"),
            URLData("https://host-two.com/child"),
            )

        val expectedResult = mutableMapOf<String, MutableList<String>>(
            urls[0].url to mutableListOf(urls[0].url, urls[1].url),
            urls[2].url to mutableListOf(urls[2].url, urls[3].url)
        )
        val result = urlPacker.pack(urls)

        assertEquals(expectedResult, result)
    }
}