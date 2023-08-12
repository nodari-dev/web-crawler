package interfaces

interface IURLHashStorage {
    fun add(hash: Int)
    fun doesNotExist(hash: Int): Boolean
}