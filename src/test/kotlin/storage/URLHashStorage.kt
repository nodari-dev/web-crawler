package storage

import storage.url.URLStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VisitedURLsTest {

    private val urlHashStorage = URLStorage

    @Test
    fun `adds new hash and allows to check if it exist`(){
        val url = "test"
        val hash = url.hashCode()
        urlHashStorage.provideURL(hash)

        val result = urlHashStorage.doesNotExist(hash)
        Assertions.assertEquals(false, result)

        val resultTwo = urlHashStorage.doesNotExist(123)
        Assertions.assertEquals(true, resultTwo)
    }
}