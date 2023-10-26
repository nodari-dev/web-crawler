package storage

import storage.interfaces.IFrontier
import storage.interfaces.IHostsStorage
import storage.interfaces.IURLStorage
import storage.mediator.StorageMediator
import storage.mediator.StorageActions.*
import core.dto.WebLink
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class StorageMediatorTest {
    private val frontierMock = mock(IFrontier::class.java)
    private val hostStorageMock = mock(IHostsStorage::class.java)
    private val urlStorageMock = mock(IURLStorage::class.java)

    private val storageMediator = StorageMediator(frontierMock, hostStorageMock, urlStorageMock)

    @Test
    fun `calls frontier pullURL`(){
        val host = "some-host"
        storageMediator.request<WebLink>(FRONTIER_PULL, host)

        verify(frontierMock).pullURL(host)
    }

    @Test
    fun `calls frontier updateOrCreateQueue`(){
        val host = "some-host"
        val url = "some-url"
        storageMediator.request<Unit>(FRONTIER_UPDATE, host, url)

        verify(frontierMock).updateOrCreateQueue(host, url)
    }

    @Test
    fun `calls frontier isQueueEmpty`(){
        val host = "some-host"
        storageMediator.request<Unit>(FRONTIER_IS_QUEUE_EMPTY, host)

        verify(frontierMock).isQueueEmpty(host)
    }

    @Test
    fun `calls frontier deleteQueue`(){
        val host = "some-host"
        storageMediator.request<Unit>(FRONTIER_DELETE_QUEUE, host)

        verify(frontierMock).deleteQueue(host)
    }

    @Test
    fun `calls hostStorage provideHost`(){
        val host = "some-host"
        storageMediator.request<Unit>(HOSTS_PROVIDE_NEW, host)

        verify(hostStorageMock).provideHost(host)
    }

    @Test
    fun `calls hostStorage deleteHost`(){
        val host = "some-host"
        storageMediator.request<Unit>(HOSTS_DELETE, host)

        verify(hostStorageMock).deleteHost(host)
    }

    @Test
    fun `calls hostStorage isURLAllowed`(){
        val host = "some-host"
        val url = "some-url"
        storageMediator.request<Boolean>(HOSTS_IS_URL_ALLOWED, host, url)

        verify(hostStorageMock).isURLAllowed(host, url)
    }

    @Test
    fun `calls urlsStorage provideURL`(){
        val hash = 123
        storageMediator.request<Boolean>(URLS_UPDATE, hash)

        verify(urlStorageMock).provideURL(hash)
    }

    @Test
    fun `calls urlsStorage doesNotExist`(){
        val hash = 123
        storageMediator.request<Boolean>(URLS_CHECK_EXISTENCE, hash)

        verify(urlStorageMock).doesNotExist(hash)
    }
}