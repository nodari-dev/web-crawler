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

    fun getNewURLs(urls: MutableList<String>): MutableList<String> {
        synchronized(mutex){
            val newURLs = mutableListOf<String>()
            for (i in urls.indices) {
                if(!urls[i].endsWith("/")){
                    newURLs.add("${urls[i]}/")
                } else{
                    newURLs.add(urls[i])
                }
            }
            return newURLs.toSet().toMutableList()
        }
    }
}