package dto

data class URLRecord(var url: String, var priorityNumber: Int = 0) {

    init {
        reformatURL()
    }

    private fun reformatURL(){
        if(!url.endsWith("/")){
            url = "$url/"
        }
    }

    fun getUniqueHash(): Int{
        return url.hashCode()
    }
}