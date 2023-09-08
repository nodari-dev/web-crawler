package storage

import org.junit.jupiter.api.AfterEach
import storage.url.URLStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import redis.RedisConnector

class VisitedURLsTest {
    private val urlStorage = URLStorage
    private val jedis = RedisConnector.getJedis()

    @BeforeEach
    fun setup(){
        jedis.flushAll()
    }

    @Test
    fun `adds new hash and allows to check if hash already exist`(){
        val someHash = 1233

        urlStorage.provideURL(someHash)

        Assertions.assertEquals(true, urlStorage.doesNotExist(33331))
        Assertions.assertEquals(false, urlStorage.doesNotExist(someHash))
    }

    @AfterEach
    fun afterEach() {
        jedis.flushAll()
    }
}