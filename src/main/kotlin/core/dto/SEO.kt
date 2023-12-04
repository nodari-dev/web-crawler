package core.dto

import kotlinx.serialization.Serializable

@Serializable
data class SEO(
    var title: String?,
    var description: String?,
    val url: String,
    var keywords: String,
    var score: Int = 0,
    var matchedAllKeywords: Boolean = false
) {
    init {
        title = title?.replace("'", "%69")
        description = description?.replace("'", "%69")
        keywords  = keywords.replace("'", "%69")
    }
}
