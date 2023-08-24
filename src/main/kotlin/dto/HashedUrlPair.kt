package dto

data class HashedUrlPair(var value: String){
    init {
        if(!value.endsWith("/")){
            value = "$value/"
        }
    }

    fun getHash(): Int{
        return value.hashCode()
    }
}
