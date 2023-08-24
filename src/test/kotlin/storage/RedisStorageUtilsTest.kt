package storage

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RedisStorageUtilsTest {
    @Test
    fun `returns correct entry path`() {
        val redisStorageUtils = RedisStorageUtils()

        val expectedResult = "default:1:2:3"
        val result = redisStorageUtils.getEntryPath("default", listOf("1", "2", "3"))
        Assertions.assertEquals(expectedResult, result)
    }
}