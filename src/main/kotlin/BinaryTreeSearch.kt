class Vertex(var value: Int) {
    var left: Vertex? = null
    var right: Vertex? = null
}
class BTS{
    private var root: Vertex? = null

    private fun addRecursive(current: Vertex?, value: Int): Vertex {
        if (current == null) {
            return Vertex(value)
        }
        if (value < current.value) {
            current.left = addRecursive(current.left, value)
        } else if (value > current.value) {
            current.right = addRecursive(current.right, value)
        } else{
            // still add value if it already exist
            current.right = addRecursive(current.right, value)
        }

        return current
    }

    fun add(value: Int): Vertex{
        root = addRecursive(root, value)
        return root as Vertex
    }

    fun inorder(node: Vertex?, list: MutableList<Int>){

        if(node == null){
            return;
        }

        inorder(node.left, list)
        list.add(node.value)
        inorder(node.right, list)

    }

    fun show(){
        if(root == null){
            println("Empty tree")
            return
        }

        val nodes = mutableListOf<Vertex>()
        nodes.add(root!!)

        while(nodes.isNotEmpty()){

            val node = nodes.removeFirst()
            print("Node ${node.value} ")

            if(node.left != null){
                nodes.add(node.left!!)
                print("L ${node.left!!.value} ")
            }

            if(node.right != null){
                nodes.add(node.right!!)
                print("R ${node.right!!.value}")
            }
            println("\n")
        }
    }
}