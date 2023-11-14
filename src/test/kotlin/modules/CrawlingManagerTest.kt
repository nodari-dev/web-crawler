//package modules
//
//import org.junit.jupiter.api.Test
//import org.mockito.Mockito.mock
//import storage.interfaces.IFrontierV2
//import kotlin.test.assertEquals
//
//class CrawlingManagerTest {
//    private val crawlerFactoryMock = mock(ICrawlerFactory::class.java)
//    private val crawlingManager = CrawlingManager(crawlerFactoryMock)
//    private val frontierMock = mock(IFrontierV2::class.java)
////    private val crawlerMock = mock(CrawlerV2(CrawlerConfig(0, "host"), frontierMock)::class.java)
//
//    @Test
//    fun `allows to request crawler initialization`(){
//        val host = "host"
////        `when`(crawlerFactoryMock.generateCrawler(CrawlerConfig(0, host))).thenReturn(crawlerMock)
//
//        // act
//        val result = crawlingManager.requestCrawlerInitializationAndGetId(host)
//        val crawlersNumber = crawlingManager.requestAllCrawlers()
//
//        assertEquals(0, result)
//        assertEquals(1, crawlersNumber)
//    }
//
//    @Test
//    fun `allows to request crawler termination`(){
//        val id = 0
//        val host = "host"
//
//        crawlingManager.requestCrawlerInitializationAndGetId(host)
//        val crawlersNumber = crawlingManager.requestAllCrawlers()
//        assertEquals(1, crawlersNumber)
//
//        crawlingManager.requestCrawlerTermination(id)
//
//        val crawlersNumberAfterDelete = crawlingManager.requestAllCrawlers()
//        assertEquals(0, crawlersNumberAfterDelete)
//    }
//
//    @Test
//    fun `allows to reassign crawler to another queue`(){
//
//    }
//}