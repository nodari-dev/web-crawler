package interfaces
import dto.URLRecord

interface ICrawlerUtils {
    fun isURLValid(url: URLRecord, host: String): Boolean
    fun canProceedCrawling(): Boolean
}