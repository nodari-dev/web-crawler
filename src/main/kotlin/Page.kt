data class Page(
    var url: String,
    var visited: Boolean = false,
    val neighbors: MutableList<Page> = mutableListOf()
) {

    init {
        if (!url.endsWith("/")) {
            url += "/"
        }
    }
}