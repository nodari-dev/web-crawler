package localStorage

import interfaces.IURLHashStorage

object VisitedURLs: IURLHashStorage{
    private val mutex = Object()
    private val storage: MutableSet<Int> = mutableSetOf()

    override fun add(value: Int){
        synchronized(mutex){
            storage.add(value)
        }
    }

    override fun doesNotExist(hash: Int): Boolean{
        synchronized(mutex){
            return !storage.contains(hash)
        }
    }

    internal fun clean(){
        storage.clear()
    }
}