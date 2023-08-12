package storages.visitedURLsStorage

object Configuration {
    const val VISITED_KEY: String = "visited"
    const val VISITED_URLS_LIST_KEY: String = "urls"
    const val DEFAULT_PATH: String = "$VISITED_KEY:$VISITED_URLS_LIST_KEY"
}