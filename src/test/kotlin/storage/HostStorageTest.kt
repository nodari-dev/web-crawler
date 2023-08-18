package storage

import dto.FormattedURL
import storage.hosts.HostsStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HostStorageTest {
    private val hostStorage = HostsStorage

    private val host = "somehost"
    private val bannedURLs = listOf(FormattedURL("url"))

    @Test
    fun `adds new host to storage and able to return`(){
    }

    @Test
    fun `checks if host is defined`(){
//
    }

    @Test
    fun `checks if URL is banned`(){
    }
}