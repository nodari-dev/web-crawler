package core.dto

data class SEO(
    val title: String?,
    val description: String?,
    val url: String,
    val keywords: Map<String, Int>
)
