package dto

data class SEOContent(
    val title: String?,
    val ogTitle: String?,
    val metaDescription: String?,
    val metaOgDescription: String?,
    val metaKeywords: List<String>,
    val imageAlts: List<String>
)
