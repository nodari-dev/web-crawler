package dto

data class HashedUrlPair(var url: String){
    init {
        if(!url.endsWith("/")){
            url = "$url/"
        }
    }

    fun getHash(): Int{
        return url.hashCode()
    }
}
