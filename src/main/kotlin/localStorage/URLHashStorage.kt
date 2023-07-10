package localStorage

import interfaces.IURLHashStorage

object URLHashStorage: IURLHashStorage{
    private val mutex = Object()
    private val values: MutableList<Int> = mutableListOf()

    override fun add(value: Int){
        synchronized(mutex){
            values.add(value)
        }
    }

    override fun alreadyExists(hash: Int): Boolean{
        synchronized(mutex){
            return values.contains(hash)
        }
    }
}