package services

import Vertex
import java.io.IOException
import java.net.URL

class Fetcher {
    fun getHTML(urlVertex: Vertex): String? {
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