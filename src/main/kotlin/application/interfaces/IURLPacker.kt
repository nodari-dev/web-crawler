package application.interfaces

import core.dto.URLData

interface IURLPacker {
    fun pack(urlDataList: List<URLData>): MutableMap<String, MutableList<String>>
}