package components.storage

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import adatapters.gateways.memoryGateways.RedisMemoryGateway
import components.storage.hosts.Configuration.DEFAULT_PATH
import components.storage.hosts.HostsStorage
import components.storage.hosts.RobotsUtils

class HostStorageTest {
    private val hostStorage = HostsStorage

    private val host = "host"
    private val bannedURLHashedPair = core.dto.WebLink("banned-url")

    private val mockRobotsUtils = Mockito.mock(RobotsUtils::class.java)
    private val mockJedis = Mockito.mock(RedisMemoryGateway::class.java)

    init {
        hostStorage.setUpTest(mockRobotsUtils, mockJedis)

        `when`(mockRobotsUtils.getDisallowedURLs(Mockito.anyString()))
            .thenAnswer { mutableListOf(bannedURLHashedPair) }
    }

    @Test
    fun `adds new host to storage and setup robots`(){
        `when`(mockJedis.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false)

        hostStorage.provideHost(host)

        verify(mockJedis).createEntry(DEFAULT_PATH, host)
        verify(mockJedis).updateEntry(DEFAULT_PATH, host, bannedURLHashedPair.url)
    }

    @Test
    fun `ignores adding the same host twice`(){
        `when`(mockJedis.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, true)

        hostStorage.provideHost(host)
        hostStorage.provideHost(host)

        verify(mockJedis).createEntry(DEFAULT_PATH, host)
        verify(mockJedis).updateEntry(DEFAULT_PATH, host,bannedURLHashedPair.url)
    }


    @Test
    fun `checks if URL is banned`(){
        `when`(mockJedis.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, true, false)

        hostStorage.provideHost(host)

        verify(mockJedis).createEntry(DEFAULT_PATH, host)
        verify(mockJedis).updateEntry(DEFAULT_PATH, host,bannedURLHashedPair.url)

        Assertions.assertEquals(false, hostStorage.isURLAllowed(host, "$host/banned-url/"))
        Assertions.assertEquals(true, hostStorage.isURLAllowed("some-new-host", "some-new-host/something"))
    }

    @Test
    fun `deletes host data`(){
        val anotherHost = "another-host"
        `when`(mockJedis.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, false)

        hostStorage.provideHost(host)
        hostStorage.provideHost(anotherHost)

        verify(mockJedis).createEntry(DEFAULT_PATH, host)
        verify(mockJedis).updateEntry(DEFAULT_PATH, host,bannedURLHashedPair.url)

        verify(mockJedis).createEntry(DEFAULT_PATH, anotherHost)
        verify(mockJedis).updateEntry(DEFAULT_PATH, anotherHost, bannedURLHashedPair.url)

        hostStorage.deleteHost(host)

        verify(mockJedis).deleteEntry(DEFAULT_PATH, host)
    }
}