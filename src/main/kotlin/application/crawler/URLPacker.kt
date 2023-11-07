package application.crawler

import application.interfaces.IURLPacker
import application.parser.urlparser.URLParser
import core.dto.URLData

class URLPacker: IURLPacker {
    private val urlParser = URLParser()

    override fun pack(urlDataList: List<URLData>): MutableMap<String, MutableList<String>> {
        val packedURLs = mutableMapOf<String, MutableList<String>>()
        urlDataList.forEach{urlData ->
            val host = urlParser.getHostWithProtocol(urlData.url)
            packedURLs.getOrPut(host, ::mutableListOf).add(urlData.url)
        }
        return packedURLs
    }
}