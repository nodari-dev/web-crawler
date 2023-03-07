import dao.FrontierDao

class Frontier: FrontierDao {
    // provide: 3 types of q to store different urls

    // set 6 queues
    // max items -> 10 per queue

    private val frontier: MutableList<String> = mutableListOf()

    override fun putURLs(urls: Array<String>) {
        frontier += urls.distinct()
    }

    override fun getURLs(): MutableList<String> {
        return frontier
    }

    fun prioritizeUrl(url: String){
        // check politeness
        // get HEAD -> check last update from PageDataStore
        // check the duplicity from PageDataStore
        //
    }
}