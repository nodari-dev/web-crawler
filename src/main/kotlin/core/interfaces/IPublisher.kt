package core.interfaces

interface IPublisher {
    fun register(subscriber: ISubscriber)
}