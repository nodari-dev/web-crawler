package storage

import storage.url.URLStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import redis.RedisConnector

class VisitedURLsTest {
    private val urlStorage = URLStorage
    private val jedis = RedisConnector.getJedis()

    private val someHash = 1233

    @BeforeEach
    fun setup(){
        jedis.flushAll()
    }

    @Test
    fun `adds new hash and allows to check if hash already exist`(){
        urlStorage.provideURL(someHash)
        Assertions.assertEquals(true, urlStorage.doesNotExist(33331))
        Assertions.assertEquals(false, urlStorage.doesNotExist(someHash))
    }
}