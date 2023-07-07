package dto

data class FrontierQueue(
    val host: HostWithProtocol,
    val urlRecords: MutableList<URLRecord>,
    var isBlocked: Boolean = false
)