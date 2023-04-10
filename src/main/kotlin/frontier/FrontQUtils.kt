package frontier

import java.util.*

class FrontQUtils (priorityId: Int, queue: Queue<String>): FrontierQueue(queue) {
    private val priorityId = priorityId

    fun getPriorityId(): Int{
        return priorityId
    }
}