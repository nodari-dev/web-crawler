package interfaces
import dto.FormattedURL

interface ICrawlerUtils {
    fun isURLNew(formattedURL: FormattedURL): Boolean
    fun canProceedCrawling(): Boolean
}