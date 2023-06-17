package crawler

import interfaces.ICounter

object Counter: ICounter {
    private val mutex = Object()
    private var value = 0

    override fun increment(){
        synchronized(mutex){
            value++
        }
    }

    override fun show(): Int{
        synchronized(mutex){
            return value
        }
    }
}