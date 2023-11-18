package application.crawler

import application.interfaces.IURLPacker
import application.parser.urlparser.URLParser
import core.dto.URLInfo

class URLPacker: IURLPacker {
    private val urlParser = URLParser()

    override fun pack(urlInfoList: List<URLInfo>): MutableMap<String, MutableList<URLInfo>> {
        val packedURLs = mutableMapOf<String, MutableList<URLInfo>>()
        urlInfoList.forEach{ urlInfo ->
            val host = urlParser.getHostname(urlInfo.link)
            packedURLs.getOrPut(host, ::mutableListOf).add(urlInfo)
        }
        return packedURLs
    }
}