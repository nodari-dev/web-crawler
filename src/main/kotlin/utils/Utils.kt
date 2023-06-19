package utils

object Utils{
    fun formatURL(url: String): String {
        return if(!url.endsWith("/")){
            "$url/"
        } else{
            url
        }
    }
}