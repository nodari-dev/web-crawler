package crawler

import frontier.Frontier

class Crawler(private val id: Int, private val frontier: Frontier): Thread(){
    override fun run() {
        while (true) {
            val url = frontier.getString()
            if(url != null){

                println("C: $id got string: $url")
                sleep(1000)

                // imitation sending new urls to frontier
                val modifiedString = "$url/abc"
                frontier.add(modifiedString)
            }
        }
    }
}