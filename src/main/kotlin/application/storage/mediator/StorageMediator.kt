package application.storage.mediator

import application.interfaces.IFrontier
import application.interfaces.IHostsStorage
import application.interfaces.IMediator
import application.interfaces.IURLStorage
import application.storage.mediator.StorageActions.*

class StorageMediator(
    private val frontier: IFrontier,
    private val hostStorage: IHostsStorage,
    private val urlStorage: IURLStorage
): IMediator {
    private val methodRegistry = hashMapOf<StorageActions, (Array<out Any>) -> Any>()

    init {
        methodRegistry[FRONTIER_PULL] = { args -> frontier.pullURL(args[0] as String) }
        methodRegistry[FRONTIER_UPDATE] = { args -> frontier.updateOrCreateQueue(args[0] as String, args[1] as String) }
        methodRegistry[FRONTIER_IS_QUEUE_EMPTY] = { args -> frontier.isQueueEmpty(args[0] as String) }
        methodRegistry[FRONTIER_DELETE_QUEUE] = { args -> frontier.deleteQueue(args[0] as String) }

        methodRegistry[HOSTS_PROVIDE_NEW] = { args -> hostStorage.provideHost(args[0] as String) }
        methodRegistry[HOSTS_DELETE] = { args -> hostStorage.deleteHost(args[0] as String) }
        methodRegistry[HOSTS_IS_URL_ALLOWED] = { args -> hostStorage.isURLAllowed(args[0] as String, args[1] as String) }

        methodRegistry[URLS_UPDATE] = { args -> urlStorage.provideURL(args[0] as Int) }
        methodRegistry[URLS_CHECK_EXISTENCE] = { args -> urlStorage.doesNotExist(args[0] as Int) }
    }

    override fun <T> request(target: StorageActions, vararg args: Any): T {
        val method = methodRegistry[target]
        @Suppress("UNCHECKED_CAST")
        return method?.invoke(args) as T
    }
}