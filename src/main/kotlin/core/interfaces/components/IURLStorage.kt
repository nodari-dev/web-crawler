package core.interfaces.components

interface IURLStorage {
    fun provideURL(hash: Int)
    fun doesNotExist(hash: Int): Boolean
}