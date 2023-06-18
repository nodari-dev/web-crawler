package parser

object URLPatterns {
    val MAIN_URL = Regex("https?:\\/\\/[^\\/]+\\/")
    const val MAIN_URL_GROUP_INDEX = 0
}