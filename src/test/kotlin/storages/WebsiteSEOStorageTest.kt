package storages

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WebsiteSEOStorageTest {
    private val websiteSEOStorage = SEOStorage

    @BeforeEach
    fun init(){
        websiteSEOStorage.clear()
    }

    @Test
    fun `adds new SEORecord and return null or record`(){
        val host = "somehost"
        websiteSEOStorage.updateOrCreateRecord(host, "123")

        val result = websiteSEOStorage.getSEORecord(host)
        val emptyResult = websiteSEOStorage.getSEORecord("someotherhost")
//        val expectedResult = SEORecord(host, "123")
//
//        Assertions.assertEquals(expectedResult, result)
//        Assertions.assertEquals(null, emptyResult)
    }

    @Test
    fun `updates existing SEORecord`(){
        val host = "somehost"
        websiteSEOStorage.updateOrCreateRecord(host, "hello")
        websiteSEOStorage.updateOrCreateRecord(host, "world!")

        val result = websiteSEOStorage.getSEORecord(host)
//        val expectedResult = SEORecord(host, "helloworld!")

//        Assertions.assertEquals(expectedResult, result)
    }
}