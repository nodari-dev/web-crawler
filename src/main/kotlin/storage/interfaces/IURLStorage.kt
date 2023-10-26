package storage.interfaces

interface IURLStorage {
    fun provideURL(hash: Int)
    fun doesNotExist(hash: Int): Boolean
}