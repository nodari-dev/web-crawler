package storage

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import storage.seo.SEOStorage

class WebsiteSEOStorageTest {
    private val websiteSEOStorage = SEOStorage

    @BeforeEach
    fun init(){
//        websiteSEOStorage.clear()
    }

    @Test
    fun `adds new SEORecord and return null or record`(){
        val host = "somehost"
    }

    @Test
    fun `updates existing SEORecord`(){
    }
}