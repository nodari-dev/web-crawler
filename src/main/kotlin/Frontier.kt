class Frontier(urls: Array<String>) {
    // provide: 3 types of q to store different urls

    fun putUrl(queue: Int, url: String){
        println("$queue, $url")
    }

    fun getUrl(queue: Int){
        println(queue)
    }

    fun prioritizeUrl(url: String){
        // check politeness
        // get HEAD -> check last update from PageDataStore
        // check the duplicity from PageDataStore
        //
    }
}