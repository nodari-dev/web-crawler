package dto

class Regex {
    companion object Values {
        const val GROUP_INDEX: Int = 2
        val A_TAG = "<a\\s+(?:[^>]*?\\s+)?href=([\"\'])(http.+?)\\1".toRegex()
        val UNSUPPORTED_FILETYPES = "/(\\.)+(css|js|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g|avi|mov|mpeg|ram|m4v|wmv|rm|smil|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx|swf|zip|rar|gz|bz2|7z|bin|xml|txt|java|c|cpp|exe)\$/g".toRegex()
    }
}
