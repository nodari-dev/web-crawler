package application.parser.urlparser
import java.util.regex.Pattern

object URLPatterns {
    val A_TAG: Pattern = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"\'])(http.+?)\\1")
    const val A_TAG_GROUP_INDEX: Int = 2

    val UNSUPPORTED_FILETYPES = listOf(".css", ".js", ".bmp", ".gif", ".jpeg", ".jpg", ".JPE", ".JPEG", ".png", ".tiff", ".tif", ".ico", ".nef", ".raw", ".mid", ".mp2", ".mp3", ".mp4", ".wav", ".wma", ".flv", ".mpeg", ".mpg", ".avi", ".mov", ".ram", ".m4v", ".wmv", ".rm", ".smil", ".pdf", ".doc", ".docx", ".pub", ".xls", ".xlsx", ".vsd", ".ppt", ".pptx", ".swf", ".zip", ".rar", ".gz", ".bz2", ".7z", ".bin", ".xml", ".txt", ".java", ".c", ".cpp", ".exe")

    val HOSTNAME: Pattern = Pattern.compile("https?:\\/\\/([^\\/]+)")
    const val HOSTNAME_GROUP_INDEX = 1
}

