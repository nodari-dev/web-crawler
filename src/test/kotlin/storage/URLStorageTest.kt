package storage

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import infrastructure.repository.RedisRepository
import storage.url.Configuration.DEFAULT_PATH
import storage.url.Configuration.PATH_KEY
import storage.url.URLStorage

class VisitedURLsTest {
    private val urlStorage = URLStorage
    private val jedisMock = mock(RedisRepository::class.java)

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

        verify(jedisMock).updateEntry(DEFAULT_PATH, PATH_KEY, someHash.toString())

        Assertions.assertEquals(true, result)
        Assertions.assertEquals(false, resultTwo)
        Assertions.assertEquals(true, resultThree)
    }
}