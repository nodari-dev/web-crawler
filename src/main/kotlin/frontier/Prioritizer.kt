package frontier

class Prioritizer {

    // 1. by protocol
    // 2. by last date of update

    fun getRandomPriority(): Double {
        val min = 1
        val max = 1000
        return Math.random()
    }
}