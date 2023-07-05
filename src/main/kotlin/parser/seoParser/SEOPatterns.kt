package parser.seoParser

object SEOPatterns {
    val META_KEYWORDS = Regex("<meta\\s+name=\"keywords\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_KEYWORDS_GROUP_INDEX: Int = 1

    val IMAGE_ALT = Regex("<img\\s+src=\"([^\"]*)\"\\s+alt=\"([^\"]*)\"\\s*>")
    const val IMAGE_ALT_GROUP_INDEX: Int = 2
}