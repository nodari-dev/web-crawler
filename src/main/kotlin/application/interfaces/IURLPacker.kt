package application.interfaces

import core.dto.URLInfo

interface IURLPacker {
    fun pack(urlInfoList: List<URLInfo>): MutableMap<String, MutableList<URLInfo>>
}