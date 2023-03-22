package services

class PagesDataStore {
    private val store: MutableList<String> = mutableListOf()


    fun wasCrawled(url: String): Boolean{
        // check if the page was crawled before
        // use simhash to check similarity BY SIGNATURE
        return url in store
    }

    fun putUrl(url: String){
        store.add(url)
    }

    fun checkStore(){
        store.forEach{item -> println(item) }
    }
}