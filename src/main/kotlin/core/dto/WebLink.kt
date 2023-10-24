package core.dto

data class WebLink(var url: String){
    init {
        if(!url.endsWith("/")){
            url = "$url/"
        }
    }

    fun getHash(): Int{
        return url.hashCode()
    }
}
