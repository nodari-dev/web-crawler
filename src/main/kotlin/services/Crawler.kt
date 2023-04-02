package services

class Crawler<T>(urls: List<T>) {
    private val seedUrls = urls

    fun start(){
//        seedUrls.forEach{seed ->
//            val startVertex: Vertex<T> = Vertex<T>(seed)
//            val queue: MutableList<Vertex<T>> = mutableListOf()
//            queue.add(startVertex)
//
//            while(queue.isNotEmpty()){
//                val current: Vertex<T>? = queue.removeFirstOrNull()
//                if (current != null) {
//                    if(!current.isVisited()){
//
//                        val html = URL(current.getData().toString()).readText()
//                        A_TAG.findAll(html).forEach { match ->
//                            val childUrl = match.groups[GROUP_INDEX]?.value
//                            if(childUrl != null){
//                                val childVertex: Vertex<T> = Vertex<T>(childUrl.toString())
//                                startVertex.setNeighbor(childVertex)
//                            }
//                        }
//
//                        current.setVisited()
//                        queue.addAll(current.getNeighbors())
//                    }
//                }
//            }
//        }


//        val html = URL(seedUrl).readText()
//        A_TAG.findAll(html).forEach { match ->
//            val childUrl = match.groups[GROUP_INDEX]?.value
//            if(childUrl != null){
//                val childVertex: Vertex<String> = Vertex(childUrl)
//                v0.setNeighbor(childVertex)
//            }
//        }
    }
}