package parser.urlParser

object URLPatterns {
    val A_TAG = Regex("<a\\s+(?:[^>]*?\\s+)?href=([\"\'])(http.+?)\\1")
    const val A_TAG_GROUP_INDEX: Int = 2

    val UNSUPPORTED_FILETYPES =
        Regex("^.+\\.(css|js|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g|avi|mov|mpeg|ram|m4v|wmv|rm|smil|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx|swf|zip|rar|gz|bz2|7z|bin|xml|txt|java|c|cpp|exe)\$")

    val MAIN_URL = Regex("https?:\\/\\/[^\\/]+\\/")
    const val MAIN_URL_GROUP_INDEX = 0
}

