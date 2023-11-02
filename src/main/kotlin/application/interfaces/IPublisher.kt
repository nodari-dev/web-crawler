package application.interfaces

interface IPublisher {
    fun register(subscriber: ISubscriber)
}