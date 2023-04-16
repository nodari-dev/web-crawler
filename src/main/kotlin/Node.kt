class Node(data: String){
    private var url: String = data
    private var visited: Boolean = false
    private val neighbors: MutableList<Node> = mutableListOf()

    init{
        if(!url.endsWith("/")){
            url += "/"
        }
    }

    fun setVisited(){
        visited = true
    }

    fun getNeighbors(): List<Node> {
        return neighbors
    }

    fun setNeighbors(newNeighbors: List<Node>){
        neighbors.addAll(newNeighbors)
    }

    fun setNeighbor(newNeighbor: Node){
        neighbors.add(newNeighbor)
    }

    fun isVisited(): Boolean{
        return visited
    }

    fun getUrl(): String{
        return url
    }
}