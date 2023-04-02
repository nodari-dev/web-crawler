class Vertex(data: String){
    private var url: String = data
    private var visited: Boolean = false
    private val neighbors: MutableList<Vertex> = mutableListOf()

    init{
        if(!url.endsWith("/")){
            url += "/"
        }
    }

    fun setVisited(){
        visited = true
    }

    fun getNeighbors(): List<Vertex> {
        return neighbors
    }

    fun setNeighbors(newNeighbors: List<Vertex>){
        neighbors.addAll(newNeighbors)
    }

    fun setNeighbor(newNeighbor: Vertex){
        neighbors.add(newNeighbor)
    }

    fun isVisited(): Boolean{
        return visited
    }

    fun getUrl(): String{
        return url
    }
}