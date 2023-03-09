package dto

data class PageInfo (
    val url: String,
    val childUrls: List<String> = listOf()
)