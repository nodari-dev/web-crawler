package dto

data class Host(val url: HostWithProtocol, val bannedURLs: List<URLRecord>)