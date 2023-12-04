package application.extractor

import application.interfaces.IDBExtractor
import core.dto.SEO
import infrastructure.repository.SEORepository

class DBExtractor(private val SEORepository: SEORepository): IDBExtractor {
    override fun execute(seo: SEO) {
        SEORepository.put(seo)
    }
}