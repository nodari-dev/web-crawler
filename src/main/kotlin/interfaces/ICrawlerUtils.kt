package interfaces
import dto.URLRecord

interface ICrawlerUtils {
    fun isURLValid(urlRecord: URLRecord, host: String): Boolean
    fun canProceedCrawling(): Boolean
}