package services

class UrlHashDataStore {
    private val storage: MutableList<Int> = mutableListOf()

    fun add(urlHash: Int){
        storage.add(urlHash)
    }

    fun includes(urlHash: Int): Boolean{
        return storage.contains(urlHash)
    }

    fun testGetAll(){
        storage.forEach{item -> println(item)}
    }
}