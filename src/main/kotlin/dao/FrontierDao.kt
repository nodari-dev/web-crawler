package dao

import dto.PageInfo

interface FrontierDao {
    fun putURLs(urls: MutableList<PageInfo>)
    fun getURLs(): MutableList<PageInfo>
}