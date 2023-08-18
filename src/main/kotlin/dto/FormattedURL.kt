package dto

data class FormattedURL(var value: String){
    init {
        if(!value.endsWith("/")){
            value = "$value/"
        }
    }

    fun getHash(): Int{
        return value.hashCode()
    }
}
