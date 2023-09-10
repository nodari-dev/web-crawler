package dto

data class HashedURLPair(var url: String){
    init {
        if(!url.endsWith("/")){
            url = "$url/"
        }
    }

    fun getHash(): Int{
        return url.hashCode()
    }
}
