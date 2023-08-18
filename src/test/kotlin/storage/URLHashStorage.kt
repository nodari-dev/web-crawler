package storage

import storage.visitedURLsStorage.VisitedURLsStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class VisitedURLsTest {

    private val urlHashStorage = VisitedURLsStorage

    @BeforeEach
    fun init(){
        urlHashStorage.clean()
    }

    @Test
    fun `adds new hash and allows to check if it exist`(){
        val url = "test"
        val hash = url.hashCode()
        urlHashStorage.add(hash)

        val result = urlHashStorage.doesNotExist(hash)
        Assertions.assertEquals(false, result)

        val resultTwo = urlHashStorage.doesNotExist(123)
        Assertions.assertEquals(true, resultTwo)
    }
}