package application.parser.robotsparser

import java.util.regex.Pattern

object RobotsPatterns {
    val DISALLOW_KEYWORD: Pattern = Pattern.compile("Disallow:\\s(\\/\\S+)")
    const val DISALLOW_KEYWORD_GROUP_INDEX: Int = 1

}