package operators.requestOperator

import application.interfaces.IURLParser
import core.dto.URLInfo
import storage.interfaces.IFrontier

class RequestHandler(
    private val frontier: IFrontier,
    private val urlParser: IURLParser
): Thread(){
    private lateinit var requestedURL: String
    private var isAlive = true

    fun requestedURL(url: String): RequestHandler {
        requestedURL = url
        return this
    }

    override fun run() {
        while(isAlive){
            val urlInfo = URLInfo(requestedURL)
            val host = urlParser.getHostname(urlInfo.link)
            frontier.update(host, listOf(urlInfo))
            isAlive = false
        }
    }
}