package crawler

class Counter(){
    private val mutex = Object()
    private var value = 0

    fun increment(){
        synchronized(mutex){
            value++
        }
    }

    fun show(): Int{
        synchronized(mutex){
            return value
        }
    }
}