package interfaces
import dto.URLRecord

interface ICrawlerUtils {
    fun isURLNew(urlRecord: URLRecord): Boolean
    fun canProceedCrawling(): Boolean
}