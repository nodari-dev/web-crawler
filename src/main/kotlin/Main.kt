import java.net.URL

fun getUrls(page: String): List<String> {
    val urls = mutableListOf<String>()
    val regex = "<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1".toRegex()
    regex.findAll(page).forEach { match -> match.groups[2]?.value?.let { urls.add(it) } }

    return urls
}

fun main(args: Array<String>) {
    val websiteLink = "https://www.tattoobar.cz/"
    val page = URL(websiteLink).readText()
    val urls = getUrls(page)
    urls.forEach{item -> if(!item.contains("./")){
        println(item)
        }
    }

//    val connection = url.openConnection()

//    BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
//        var line: String?
//        while (inp.readLine().also { line = it } != null) {
//
//            println(line)
//        }
//    }
}