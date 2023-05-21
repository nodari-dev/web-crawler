package crawler

import frontier.Frontier

class Crawler(private val id: Int, private val frontier: Frontier): Thread(){
    // imitation of locking to specific host
    private val host = "host$id.com"

    override fun run() {
        while (true) {
            val url = frontier.getString()
            if(url != null){
//            if(isValidUrl(url)){

                println("C: $id got string: $url")
                sleep(1000)

                send(url)
            }
        }
    }

    private fun send(url: String){
        // imitation sending new urls to frontier
        val modifiedString = "$url/abc"
        frontier.add(modifiedString)
    }

    private fun isValidUrl(url: String?): Boolean{
        return url != null && url.contains(host)
    }
}