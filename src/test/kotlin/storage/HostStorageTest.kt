package storage

import dto.HashedUrlPair
import storage.hosts.HostsStorage
import org.junit.jupiter.api.Test

class HostStorageTest {
    private val hostStorage = HostsStorage

    private val host = "somehost"
    private val bannedURLs = listOf(HashedUrlPair("url"))

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