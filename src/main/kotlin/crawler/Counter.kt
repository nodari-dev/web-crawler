package crawler

import interfaces.ICounter

object Counter: ICounter {
    private val mutex = Object()
    var value = 0

    override fun increment(){
        synchronized(mutex){
            value++
        }
    }
}