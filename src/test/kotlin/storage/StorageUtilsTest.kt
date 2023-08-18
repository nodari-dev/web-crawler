package storage

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class StorageUtilsTest {
    @Test
    fun `returns correct entry path`() {
        val storageUtils = StorageUtils()

        val expectedResult = "default:1:2:3"
        val result = storageUtils.getEntryPath("default", listOf("1", "2", "3"))
        Assertions.assertEquals(expectedResult, result)
    }
}