package core.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val results: List<SEO>,
    val totalPages: Int,
)
