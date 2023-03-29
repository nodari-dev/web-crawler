package services
import dao.Regex.Values.A_TAG
import dao.Regex.Values.GROUP_INDEX
import java.net.URL

class Crawler(urls: List<String>) {
    private val seedUrls = urls

    fun start(){
    }
}

//A_TAG.findAll(html).forEach { match ->
//    val value = match.groups[GROUP_INDEX]?.value
//    if (value != null) {
//        test.add(value)
//    }
//}