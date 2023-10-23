package adapters.gateways.memoryGateways

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import adatapters.gateways.memoryGateways.RedisStorageUtils

class RedisStorageUtilsTest {
    @Test
    fun `returns correct entry path`() {
        val redisStorageUtils = RedisStorageUtils()

        val expectedResult = "default:123"
        val result = redisStorageUtils.getEntryPath("default", "123")
        Assertions.assertEquals(expectedResult, result)
    }
}