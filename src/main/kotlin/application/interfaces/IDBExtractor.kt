package application.interfaces

import core.dto.SEO

interface IDBExtractor {
    fun execute(seo: SEO)
}