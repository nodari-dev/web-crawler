package operators.requestOperator

import application.parser.urlparser.URLParser
import storage.interfaces.IFrontier

class RequestsOperator(
    private val frontier: IFrontier){
    private val urlParser = URLParser()

    fun handleRequest(url: String){
        val requestHandle = RequestHandler(frontier, urlParser).requestedURL(url)
        requestHandle.start()
        requestHandle.join()
    }
}