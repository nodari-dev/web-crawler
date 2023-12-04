package operators

import core.dto.SEO
import infrastructure.repository.interfaces.ISEORepository

class SearchOperator(private val sqliteRepository: ISEORepository){
    fun searchAndSortByPriority(requestString: String): List<SEO>{
        val foundResults = sqliteRepository.search(requestString.lowercase())

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

        return scoredResults.sortedWith(compareByDescending<SEO>{it.matchedAllKeywords}.thenByDescending { it.score })
    }
}