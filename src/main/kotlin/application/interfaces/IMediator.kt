package application.interfaces

import storage.mediator.Actions

interface IMediator {
    fun <T> request(target: Actions, vararg args: Any): T
}