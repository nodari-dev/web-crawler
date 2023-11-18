package core.dto

data class URLInfo(
    var link: String,
    var hash: String = ""
) {
    init {
        if(!link.endsWith("/")){
            link = "$link/"
        }
        hash = link.hashCode().toString()
    }
}
