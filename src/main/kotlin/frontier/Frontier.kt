package frontier

object Frontier {
    private val mutex = Object()

    private val storage = mutableListOf<String>()

    fun getUrl(): String?{
        synchronized(mutex){
            return storage.removeLastOrNull()
        }
    }

    fun addUrls(urls: List<String>) {
        synchronized(mutex){
            println("Added ${urls.size} urls")
            urls.forEach{url ->
                storage.add(url)
            }
        }
    }
    // IObservable


    // 1. Each queue must have MAX number of urls
    // 2. Each back-queue must have NAME AS A HOST NAME
    // 3. Each back-queue contains ONLY URLS with the same host
    // 4. If url has unrecognisible host -> create new queue with NEW HOST NAME
    // 5. Frontier works only with NEW URLS
    // 6. is urls was seen -> it will be refetched later (schedule)

    // after fetch -> put all found urls to frontier
    // frontier will handle all urls
    // frontier works on separated thread

}