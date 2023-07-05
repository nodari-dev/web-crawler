package utils

class Utils{
    fun formatURL(url: String): String {
        return if(!url.endsWith("/")){
            "$url/"
        } else{
            url
        }
    }
}