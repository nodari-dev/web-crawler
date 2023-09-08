package storage

import dto.HashedURLPair
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import storage.hosts.HostsStorage
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import redis.RedisConnector
import storage.hosts.Configuration.DEFAULT_PATH
import storage.hosts.RobotsUtils

class HostStorageTest {
    private val hostStorage = HostsStorage
    private val jedis = RedisConnector.getJedis()
    private val testUtils = TestUtils()

    private val host = "host"
    private val bannedURL = "banned-url"

    private val mockRobotsUtils = Mockito.mock(RobotsUtils::class.java)

    init {
        hostStorage.setUpTest(mockRobotsUtils)

        `when`(mockRobotsUtils.getDisallowedURLs(Mockito.anyString()))
            .thenAnswer { mutableListOf(HashedURLPair(bannedURL)) }
    }

    @BeforeEach
    fun `set up`(){
        jedis.flushAll()
    }

    @Test
    fun `adds new host to storage and setup robots`(){
        hostStorage.provideHost(host)
        Assertions.assertEquals(mutableListOf(host), testUtils.getDefaultPathContent(DEFAULT_PATH))
        Assertions.assertEquals(mutableListOf(HashedURLPair(bannedURL).url), testUtils.getDefaultPathChildContent(DEFAULT_PATH, host))
    }

    @Test
    fun `ignores adding the same host twice`(){
        hostStorage.provideHost(host)
        hostStorage.provideHost(host)
        Assertions.assertEquals(mutableListOf(host), testUtils.getDefaultPathContent(DEFAULT_PATH))
        Assertions.assertEquals(mutableListOf(HashedURLPair(bannedURL).url), testUtils.getDefaultPathChildContent(DEFAULT_PATH, host))
    }


    @Test
    fun `checks if URL is banned`(){
        hostStorage.provideHost(host)
        Assertions.assertEquals(false, hostStorage.isURLAllowed(host, "$host/banned-url/"))
        Assertions.assertEquals(true, hostStorage.isURLAllowed(host, "$host/something"))
        Assertions.assertEquals(true, hostStorage.isURLAllowed("some-new-host", "some-new-host/something"))
    }

    @Test
    fun `deletes host data`(){
        hostStorage.provideHost(host)
        hostStorage.provideHost("some-new")
        hostStorage.deleteHost(host)
        Assertions.assertEquals(mutableListOf("some-new"), testUtils.getDefaultPathContent(DEFAULT_PATH))
        Assertions.assertEquals(mutableListOf<String>(), testUtils.getDefaultPathChildContent(DEFAULT_PATH, host))
    }
}