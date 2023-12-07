package operators

import core.dto.SEO
import core.dto.SearchResult
import infrastructure.repository.interfaces.ISEORepository

class SearchOperator(private val seoRepository: ISEORepository){
    fun searchAndSortByPriority(requestString: String, page: Int, size: Int): SearchResult{
        val foundResults = seoRepository.search(requestString.lowercase())
        val keywordsFromRequest = requestString.split(" ")
        val scoredResults = mutableListOf<SEO>()

        foundResults.forEach { result ->
            val resultKeywords = result.keywords.split(",")
            var score = 0
            var wordsMatched = 0

            keywordsFromRequest.forEach { keyword ->
                resultKeywords.forEachIndexed{ index, resultKeyword ->
                    if(resultKeyword.contains(keyword)) {
                        score += index
                        wordsMatched += 1
                        return@forEachIndexed
                    }
                }
            }
            result.matchedAllKeywords = wordsMatched == keywordsFromRequest.size
            result.score = score
            scoredResults.add(result)
        }

        val sortedResults = scoredResults
            .sortedWith(compareByDescending<SEO>{it.matchedAllKeywords}
            .thenByDescending { it.score })
            .toSet()
            .toList()
        return if (sortedResults.size < 0 || size <= 0 || page <= 0){
            SearchResult(emptyList(), 0)
        } else{
            paginateResults(sortedResults, page, size)
        }
    }

    private fun paginateResults(results: List<SEO>, page: Int, size: Int): SearchResult{
        val total = results.size

        val startIndex = (page - 1) * size
        val endIndex = minOf(startIndex + size, total)

        if(startIndex >= total){
            return SearchResult(emptyList(), 0)
        }

        val maxPages = (total + size - 1) / size
        return SearchResult(results.subList(startIndex, endIndex), maxPages)
    }
}