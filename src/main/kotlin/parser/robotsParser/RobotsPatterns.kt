package parser.robotsParser

object RobotsPatterns {
    val DISALLOW_KEYWORD = Regex("Disallow:\\s(\\/\\S+)")
    const val DISALLOW_KEYWORD_GROUP_INDEX: Int = 1

}