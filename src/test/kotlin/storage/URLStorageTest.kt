package storage

import storage.url.URLStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.RedisManager
import storage.url.Configuration.DEFAULT_PATH

class VisitedURLsTest {
    private val urlStorage = URLStorage
    private val jedisMock = mock(RedisManager::class.java)

    init {
        urlStorage.setup(jedisMock)
    }

    @Test
    fun `adds new hash and allows to check if hash already exist`(){
        val someHash = 1233
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, someHash.toString())).thenReturn(false, true, false)

        val result = urlStorage.doesNotExist(someHash)
        urlStorage.provideURL(someHash)
        val resultTwo = urlStorage.doesNotExist(someHash)
        val resultThree = urlStorage.doesNotExist(33331)

        verify(jedisMock).updateEntry(DEFAULT_PATH, someHash.toString())

        Assertions.assertEquals(true, result)
        Assertions.assertEquals(false, resultTwo)
        Assertions.assertEquals(true, resultThree)
    }
}