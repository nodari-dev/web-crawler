package interfaces

interface IURLHashStorage {
    fun add(value: Int)
    fun alreadyExists(hash: Int): Boolean
}