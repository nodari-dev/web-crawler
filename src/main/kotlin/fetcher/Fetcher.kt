package fetcher

import Vertex
import java.io.IOException
import java.net.URL

class Fetcher {
    fun getHTML(urlVertex: Vertex): String? {
//        Timer().schedule(object : TimerTask() {
//            override fun run() {
//                TODO("ADD TIMEOUT FROM CONFIGURATION")
//            }
//        }, 2000)
        return try{

            URL(urlVertex.getUrl()).readText()
        } catch (e: IOException){
            println("Could not parse document: $e")
            null
        }
    }

    fun getHEAD(){
        // to get data (last updated, etc...)
    }
}