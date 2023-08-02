package frontier

import dto.FrontierQueue

class QueueUtils {

    fun isQueueDefined(queues: MutableList<FrontierQueue>, host: String): Boolean{
        return queues.any { queue -> queue.host == host }
    }
}