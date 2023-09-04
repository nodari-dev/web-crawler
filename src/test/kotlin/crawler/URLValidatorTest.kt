package crawler

import dto.HashedURLPair
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import storage.hosts.HostsStorage
import storage.url.URLStorage

class URLValidatorTest {
    private val urlValidator = URLValidator()

    private val mockHostsStorage = Mockito.mock(HostsStorage::class.java)
    private val mockUrlStorage = Mockito.mock(URLStorage::class.java)

    init {
        urlValidator.hostsStorage = mockHostsStorage
        urlValidator.urlStorage = mockUrlStorage
    }

    @Test
    fun `returns false when hashedUrlPair is null`() {
        val result = urlValidator.canProcessURL("something", null)
        Assertions.assertEquals(false, result)
    }

    @Test
    fun `returns true when url is not banned and not visited`() {
        Mockito.`when`(mockUrlStorage.doesNotExist(Mockito.anyInt()))
            .thenAnswer { true }

        Mockito.`when`(mockHostsStorage.isURLAllowed(Mockito.anyString(), Mockito.anyString()))
            .thenAnswer { true }

        val result = urlValidator.canProcessURL("something", HashedURLPair("someURL"))
        Assertions.assertEquals(true, result)
    }

    @Test
    fun `returns false when url is banned`() {
        Mockito.`when`(mockUrlStorage.doesNotExist(Mockito.anyInt()))
            .thenAnswer { true }

        Mockito.`when`(mockHostsStorage.isURLAllowed(Mockito.anyString(), Mockito.anyString()))
            .thenAnswer { false }

        val result = urlValidator.canProcessURL("something", HashedURLPair("someURL"))
        Assertions.assertEquals(false, result)
    }

    @Test
    fun `returns false when url is already visited`() {
        Mockito.`when`(mockUrlStorage.doesNotExist(Mockito.anyInt()))
            .thenAnswer { false }

        Mockito.`when`(mockHostsStorage.isURLAllowed(Mockito.anyString(), Mockito.anyString()))
            .thenAnswer { true }

        val result = urlValidator.canProcessURL("something", HashedURLPair("someURL"))
        Assertions.assertEquals(false, result)
    }
}