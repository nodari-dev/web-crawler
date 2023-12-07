package application.crawler

import core.dto.URLInfo
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class URLPackerTest {
    private val urlPacker = URLPacker()

    @Test
    fun `packs urls related to specific host`(){
        val urls = listOf(
            URLInfo("https://host-one.com"),
            URLInfo("https://host-one.com/child"),
            URLInfo("https://host-two.com"),
            URLInfo("https://host-two.com/child"),
            )

        val expectedResult = mutableMapOf(
            "host-one.com" to mutableListOf(urls[0], urls[1]),
            "host-two.com" to mutableListOf(urls[2], urls[3])
        )
        val result = urlPacker.pack(urls)

        assertEquals(expectedResult, result)
    }
}