package application.storage.frontier

class Prioritizer {

    // 1. by protocol
    // 2. by last date of update
    // 3. by number of mentions per one page
    // 4. by level of depth: www.host.com (0 layer) and so on

    fun getRandomPriority(): Double {
        val min = 1
        val max = 1000
        return Math.random()
    }
}