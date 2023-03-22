package dao

import dto.PageInfo

interface FrontierDao {
    fun putURL(url: PageInfo)
    fun putListOfURLs(url: MutableList<PageInfo>)
    fun getURLs(): MutableList<PageInfo>
}