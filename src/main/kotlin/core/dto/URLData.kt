package core.dto

data class URLData(
    var url: String
){
    init {
        if(!url.endsWith("/")){
            url = "$url/"
        }
    }

    fun getHashedURL(): Int{
        return url.hashCode()
    }
}
