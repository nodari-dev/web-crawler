package dto

data class FrontierQueue(
    val host: String,
    val frontierRecords: MutableList<FrontierRecord>,
)