package dto

data class URLRecord(var formattedURL: FormattedURL) {
    fun getUniqueHash(): Int{
        return formattedURL.value.hashCode()
    }

    fun getURL(): String{
        return formattedURL.value
    }
}