package interfaces
import dto.FormattedURL

interface ICrawlerUtils {
    fun canProcessURL(host: String, formattedURL: FormattedURL?): Boolean
    fun canProceedCrawling(): Boolean
}