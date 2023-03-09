import dao.FrontierDao
import dto.Page
import dto.PageInfo

class Frontier: FrontierDao {
    private val pagesStore = PagesDataStore()
    private val frontier: MutableList<PageInfo> = mutableListOf()

    // provide: 3 types of q to store different urls

    // 2. Check the identity of page by PageDataStore
    // 3. first time -> create signature

    // set 6 queues
    // max items -> 10 per queue


    override fun putURLs(urls: MutableList<PageInfo>) {
        frontier += urls
    }

    override fun getURLs(): MutableList<PageInfo> {
        return frontier
    }

//
//    init{
//        // check url before fetching
//
//        seedUrls.forEach { url ->
//            if(!pagesStore.wasCrawled(url)){
//                pagesStore.putUrl(url)
////                getUrls(URL(url).readText())
//            }
//        }
//        pagesStore.checkStore()
//    }
//
    fun prioritizeUrl(url: String){
        // check politeness
        // get HEAD -> check last update from PageDataStore
        // check the duplicity from PageDataStore
        //
    }
}