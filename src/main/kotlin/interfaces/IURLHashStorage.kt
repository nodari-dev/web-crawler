package interfaces

interface IURLHashStorage {
    fun add(value: Int)
    fun alreadyExists(value: Int): Boolean
}