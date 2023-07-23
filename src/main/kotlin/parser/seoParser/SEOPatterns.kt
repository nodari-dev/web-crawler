package parser.seoParser

object SEOPatterns {
    val TITLE = Regex("<title>([^\"]*)<\\/title>")
    const val TITLE_GROUP_INDEX: Int = 1

    val META_OG_TITLE = Regex("<meta\\s+property=\"og:title\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_OG_TITLE_GROUP_INDEX: Int = 1

    val META_OG_DESCRIPTION = Regex("<meta\\s+property=\"og:description\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_OG_DESCRIPTION_GROUP_INDEX: Int = 1

    val META_DESCRIPTION = Regex("<meta\\s+name=\"description\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_DESCRIPTION_GROUP_INDEX: Int = 1

    val META_KEYWORDS = Regex("<meta\\s+name=\"keywords\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_KEYWORDS_GROUP_INDEX: Int = 1

    val IMAGE_ALT = Regex("<img\\s+src=\"([^\"]*)\"\\s+alt=\"([^\"]*)\"\\s*>")
    const val IMAGE_ALT_GROUP_INDEX: Int = 2
}