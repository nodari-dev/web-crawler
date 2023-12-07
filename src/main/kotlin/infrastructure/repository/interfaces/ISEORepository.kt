package infrastructure.repository.interfaces

import core.dto.SEO

interface ISEORepository {
    fun put(seo: SEO)
    fun search(requestedString: String): MutableList<SEO>
}