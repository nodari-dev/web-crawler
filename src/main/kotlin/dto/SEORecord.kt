package dto

data class SEORecord(val host: String, var data: MutableList<SEORecordItem>)

data class SEORecordItem(val url: FormattedURL, val content: SEOContent)

data class SEOContent(
    val title: String?,
    val ogTitle: String?,
    val metaDescription: String?,
    val metaOgDescription: String?,
    val metaKeywords: List<String>,
    val imageAlts: List<String>
)