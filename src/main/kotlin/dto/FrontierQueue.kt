package dto

data class FrontierQueue(val host: String, val urls: MutableList<String>, var isBlocked: Boolean = false)