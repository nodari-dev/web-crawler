package frontier

class Frontier {
    // 2. Each back-queue must have NAME AS A HOST NAME
    // 3. Each back-queue contains ONLY URLS with the same host
    // 4. If url has unrecognisible host -> create new queue with NEW HOST NAME
    // 5. Frontier works only with NEW URLS
    // 6. is urls was seen -> it will be refetched later (schedule)

    // after fetch -> put all found urls to frontier
    // frontier will handle all urls
    // frontier works on separated thread

    private val urls = mutableListOf("host0.com", "host1.com", "host2.com",)
    private val mutex = Object()

    fun add(value: String) {
        synchronized(mutex){
            urls.add(value)
            mutex.notifyAll()
        }
    }

    private fun removeString(): String? {
        return urls.removeFirstOrNull()
    }

    fun getString(): String? {
        synchronized(mutex) {
            while (urls.isEmpty()) {
                mutex.wait()
            }
            val value = removeString()

            if(value != null){
                return value
            }
            return null
        }
    }
}