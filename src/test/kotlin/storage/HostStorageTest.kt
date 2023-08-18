package storage

import dto.FormattedURL
import storage.hostsStorage.HostsStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HostStorageTest {
    private val hostStorage = HostsStorage

    private val host = "somehost"
    private val bannedURLs = listOf(FormattedURL("url"))

    @BeforeEach
    fun init(){
        hostStorage.clear()
    }

    @Test
    fun `adds new host to storage and able to return`(){
        hostStorage.addHostRecord(host, bannedURLs)

        val expectedResult = HostRecord(host, bannedURLs)
        val result = hostStorage.getHostRecord(host)
        Assertions.assertEquals(expectedResult, result)

        val resultTwo = hostStorage.getHostRecord("test")
        Assertions.assertEquals(null, resultTwo)
    }

    @Test
    fun `checks if host is defined`(){
        hostStorage.addHostRecord(host, bannedURLs)

        val result = hostStorage.isHostDefined(host)
        Assertions.assertEquals(true, result)

        val resultTwo = hostStorage.isHostDefined("test")
        Assertions.assertEquals(false, resultTwo)
    }

    @Test
    fun `checks if URL is banned`(){
        hostStorage.addHostRecord(host, bannedURLs)

        val result = hostStorage.isURLAllowed(host, bannedURLs[0].value)
        val resultTwo = hostStorage.isURLAllowed(host, "somethingElse")
        Assertions.assertEquals(false, result)
        Assertions.assertEquals(true, resultTwo)
    }
}