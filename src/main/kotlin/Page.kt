data class Page(
    var url: String,
    val hash: Int = url.hashCode(),
    var visited: Boolean = false,
    val neighbors: MutableList<Page> = mutableListOf(),
    var html: String? = null
) {

    init {
        if (!url.endsWith("/")) {
            url += "/"
        }
        if(url.contains("www.")){
            url = url.replace("www.", "")
        }
    }
}