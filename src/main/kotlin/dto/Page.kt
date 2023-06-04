package dto

data class Page(
    var url: String,
    val hash: Int = url.hashCode(),
    val neighbors: MutableList<Page> = mutableListOf(),
    var html: String? = null
) {

    init {
        if (!url.endsWith("/")) {
            url += "/"
        }
    }
}