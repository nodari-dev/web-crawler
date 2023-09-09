package redis

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.clients.jedis.JedisPool

class RedisManagerTest {
    private val redisManager = RedisManager
    private val jedisMock = mock(JedisPool("localhost", 6379).resource::class.java)

    private val primaryPath = "primaryPath"
    private val path = "path"
    private val secondPath = "secondPath"
    private val key = "key"

    init{
        redisManager.setupTest(jedisMock)
    }

    @Test
    fun `createEntry works correct`(){
        redisManager.createEntry(path, key)

        verify(jedisMock).lpush(path, key)
        verify(jedisMock).close()
    }

    @Test
    fun `updateEntry works correct`(){
        redisManager.updateEntry(path, key)

        verify(jedisMock).rpush(path, key)
        verify(jedisMock).close()
    }

    @Test
    fun `deleteEntry works correct`(){
        redisManager.deleteEntry(primaryPath, path, key)

        verify(jedisMock).del(path)
        verify(jedisMock).lrem(primaryPath, 1, key)
        verify(jedisMock).close()
    }

    @Test
    fun `getFirstEntryItem works correct`(){
        `when`(jedisMock.lpop(path)).thenReturn("something")

        val result = redisManager.getFirstEntryItem(path)

        Assertions.assertEquals("something", result)
        verify(jedisMock).lpop(path)
        verify(jedisMock).close()
    }

    @Test
    fun `getListOfEntryKeys works correct`(){
        `when`(jedisMock.lrange(path, 0, -1)).thenReturn(listOf("one", "two"))

        val result = redisManager.getListOfEntryKeys(path)

        Assertions.assertEquals(listOf("one", "two"), result)
        verify(jedisMock).lrange(path, 0, -1)
        verify(jedisMock).close()
    }

    @Test
    fun `checkEntryEmptiness works correct`(){
        `when`(jedisMock.lrange(path, 0, -1)).thenReturn(listOf("1", "2"))
        `when`(jedisMock.lrange(secondPath, 0, -1)).thenReturn(emptyList())

        val result = redisManager.checkEntryEmptiness(path)
        val resultTwo = redisManager.checkEntryEmptiness(secondPath)

        Assertions.assertEquals(false, result)
        Assertions.assertEquals(true, resultTwo)

        verify(jedisMock).lrange(path, 0, -1)
        verify(jedisMock).lrange(secondPath, 0, -1)
        verify(jedisMock, times(2)).close()
    }

    @Test
    fun `isEntryKeyDefined works correct`(){
        `when`(jedisMock.lpos(path, key)).thenReturn(1)

        val result = redisManager.isEntryKeyDefined(path, key)

        Assertions.assertEquals(true, result)

        verify(jedisMock).lpos(path, key)
        verify(jedisMock).close()
    }

    @Test
    fun `clear works correct`(){
        redisManager.clear()

        verify(jedisMock).flushAll()
        verify(jedisMock).close()
    }
}