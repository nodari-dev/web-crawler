package robots

import interfaces.IRobots

class Robots: IRobots{
    override fun getDisallowedURLs(): List<String> {
        return listOf("123")
    }

    private fun getRobotsURL(){

    }
}