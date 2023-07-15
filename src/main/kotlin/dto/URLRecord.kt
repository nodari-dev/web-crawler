package dto

data class URLRecord(private var formattedURL: FormattedURL, var priorityNumber: Int = 0) {
    fun getUniqueHash(): Int{
        return formattedURL.value.hashCode()
    }

    fun getURL(): String{
        return formattedURL.value
    }
}