package dto

data class URLRecord(var url: String, var priorityNumber: Int = 0) {
    init {
        reformatURL()
    }

    private fun reformatURL(){
        if(!url.endsWith("/")){
            "$url/"
        } else{
            url
        }
    }

    fun uniqueHash(): UniqueHash{
        return UniqueHash(url.hashCode())
    }
}