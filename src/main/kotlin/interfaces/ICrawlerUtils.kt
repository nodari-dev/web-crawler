package interfaces
import dto.FormattedURL

interface ICrawlerUtils {
    fun isURLValid(host: String, formattedURL: FormattedURL): Boolean
    fun canProceedCrawling(): Boolean
}