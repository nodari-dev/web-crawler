package interfaces
import dto.HostWithProtocol
import dto.URLRecord

interface ICrawlerUtils {
    fun isURLValid(url: URLRecord, hostWithProtocol: HostWithProtocol): Boolean
    fun canProceedCrawling(): Boolean
}