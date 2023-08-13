package parser.seoParser

import java.util.regex.Pattern

object SEOPatterns {
    val TITLE: Pattern =  Pattern.compile("<title>([^\"]*)<\\/title>")
    const val TITLE_GROUP_INDEX: Int = 1

    val META_OG_TITLE: Pattern =  Pattern.compile("<meta\\s+property=\"og:title\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_OG_TITLE_GROUP_INDEX: Int = 1

    val META_OG_DESCRIPTION: Pattern =  Pattern.compile("<meta\\s+property=\"og:description\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_OG_DESCRIPTION_GROUP_INDEX: Int = 1

    val META_DESCRIPTION: Pattern =  Pattern.compile("<meta\\s+name=\"description\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_DESCRIPTION_GROUP_INDEX: Int = 1

    val META_KEYWORDS: Pattern =  Pattern.compile("<meta\\s+name=\"keywords\"\\s+content=\"([^\"]*)\"\\s*>")
    const val META_KEYWORDS_GROUP_INDEX: Int = 1

    val IMAGE_ALT: Pattern =  Pattern.compile("<img\\s+src=\"([^\"]*)\"\\s+alt=\"([^\"]*)\"\\s*>")
    const val IMAGE_ALT_GROUP_INDEX: Int = 2
}