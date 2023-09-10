import seedsManager.SeedsManager

fun main() {
    SeedsManager.startCrawling(listOf("https://en.wikipedia.org/wiki/Main_Page"))
}