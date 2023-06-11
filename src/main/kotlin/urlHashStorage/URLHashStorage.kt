package urlHashStorage

import interfaces.IURLHashStorage

class URLHashStorage(): IURLHashStorage{
    private val mutex = Object()
    private val values: MutableList<Int> = mutableListOf()

    override fun add(value: Int){
        synchronized(mutex){
            values.add(value)
        }
    }

    override fun alreadyExists(value: Int): Boolean{
        synchronized(mutex){
            return values.contains(value)
        }
    }
}