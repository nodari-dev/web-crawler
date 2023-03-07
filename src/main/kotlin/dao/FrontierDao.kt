package dao

interface FrontierDao {
    fun putURLs(urls: Array<String>)
    fun getURLs(): MutableList<String>
}