package dto

data class SEOContent(
    val title: String,
    val description: String,
    val url: String,
    val keywords: List<String>
)
