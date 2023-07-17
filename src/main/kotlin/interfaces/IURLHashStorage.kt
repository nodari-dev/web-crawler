package interfaces

interface IURLHashStorage {
    fun add(value: Int)
    fun doesNotExist(hash: Int): Boolean
}