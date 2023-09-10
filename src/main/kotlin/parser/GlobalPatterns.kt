package parser

import java.util.regex.Pattern

object GlobalPatterns {
    val NESTED_TAGS: Pattern = Pattern.compile("<[^>]+>")
}