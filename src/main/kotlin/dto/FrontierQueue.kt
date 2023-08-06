package dto

data class FrontierQueue(
    val host: String,
    val urlRecords: MutableList<URLRecord>,
//    var isBlocked: Boolean = false
)