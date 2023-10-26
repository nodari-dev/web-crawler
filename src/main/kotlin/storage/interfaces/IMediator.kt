package storage.interfaces

import storage.mediator.StorageActions

interface IMediator {
    fun <T> request(target: StorageActions, vararg args: Any): T
}