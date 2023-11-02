package infrastructure.gateways.memoryGateways

import infrastructure.repository.RedisRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.clients.jedis.JedisPool
import java.util.concurrent.locks.ReentrantLock

class RedisRepositoryTest {
    private val redisManager = RedisRepository
    private val jedis = RedisRepository
    private val jedisMock = mock(JedisPool("localhost", 6379).resource::class.java)
    private val mutexMock = mock(ReentrantLock::class.java)

    private val path = "path"
    private val secondPath = "secondPath"
    private val key = "key"
    private val value = "value"

    private val combinedPath = "$path:$key"
    private val secondCombinedPath = "$secondPath:$key"

    init{
        redisManager.setupTest(jedisMock, mutexMock)
    }

    @AfterEach
    fun cleanup(){
        jedis.clear()
    }

    @Test
    fun `createEntry works correct`(){
        redisManager.createEntry(path, key)

        verify(mutexMock).lock()
        verify(jedisMock).lpush(path, key)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `updateEntry works correct`(){
        redisManager.updateEntry(path, key, value)

        verify(mutexMock).lock()
        verify(jedisMock).rpush("$path:$key", value)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `deleteEntry works correct`(){
        redisManager.deleteEntry(path, key)

        verify(mutexMock).lock()
        verify(jedisMock).del("$path:$key")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `getFirstEntryItem works correct`(){
        `when`(jedisMock.lpop(combinedPath)).thenReturn("something")

        val result = redisManager.getFirstEntryItem(path, key)

        Assertions.assertEquals("something", result)
        verify(mutexMock).lock()
        verify(jedisMock).lpop("$path:$key")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `getListOfEntryKeys works correct`(){
        `when`(jedisMock.lrange(combinedPath, 0, -1)).thenReturn(listOf("one", "two"))

        val result = redisManager.getListOfEntryKeys(path, key)

        Assertions.assertEquals(listOf("one", "two"), result)
        verify(mutexMock).lock()
        verify(jedisMock).lrange(combinedPath, 0, -1)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `checkEntryEmptiness works correct`(){
        `when`(jedisMock.lrange(combinedPath, 0, -1)).thenReturn(listOf("1", "2"))
        `when`(jedisMock.lrange(secondCombinedPath, 0, -1)).thenReturn(emptyList())

        val result = redisManager.checkEntryEmptiness(path, key)
        val resultTwo = redisManager.checkEntryEmptiness(secondPath, key)

        Assertions.assertEquals(false, result)
        Assertions.assertEquals(true, resultTwo)

        verify(mutexMock, times(2)).lock()
        verify(jedisMock).lrange(combinedPath, 0, -1)
        verify(jedisMock).lrange(secondCombinedPath, 0, -1)
        verify(jedisMock, times(2)).close()
        verify(mutexMock, times(2)).unlock()
    }

    @Test
    fun `isEntryKeyDefined works correct`(){
        `when`(jedisMock.lpos(path, key)).thenReturn(1)

        val result = redisManager.isEntryKeyDefined(path, key)

        Assertions.assertEquals(true, result)

        verify(mutexMock).lock()
        verify(jedisMock).lpos(path, key)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `clear works correct`(){
        redisManager.clear()

        verify(mutexMock).lock()
        verify(jedisMock).flushAll()
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @AfterEach
    fun afterEach() {
        jedis.clear()
    }
}