fun main() {
    val seedUrls: List<String> = listOf(
        "https://cs.wikipedia.org/",
    ).distinct()

    val v0 = Vertex(seedUrls[0])
    val bfs = BreathFirstSearch(v0)

    bfs.traverse()

//    if(seedUrls.isNotEmpty()){
//        val crawler = Crawler<>(seedUrls)
//    } else{
//        println("No seed urls provided")
//    }
}