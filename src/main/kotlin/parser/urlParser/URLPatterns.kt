package parser.urlParser
import java.util.regex.Pattern

object URLPatterns {
    val A_TAG: Pattern = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"\'])(http.+?)\\1")
    const val A_TAG_GROUP_INDEX: Int = 2

    val UNSUPPORTED_FILETYPES: Pattern = Pattern.compile("^.+\\.(css|js|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g|avi|mov|mpeg|ram|m4v|wmv|rm|smil|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx|swf|zip|rar|gz|bz2|7z|bin|xml|txt|java|c|cpp|exe)\$")

    val HOST_WITH_PROTOCOL: Pattern = Pattern.compile("https?:\\/\\/[^\\/]+")
    const val HOST_WITH_PROTOCOL_GROUP_INDEX = 0
}

