package server

import application.extractor.Extractor
import configuration.Configuration.HOST
import configuration.Configuration.PORT
import infrastructure.gateways.SQLiteGateway
import infrastructure.repository.FrontierRepository
import infrastructure.repository.RobotsRepository
import infrastructure.repository.SEORepository
import infrastructure.repository.VisitedURLsRepository
import io.ktor.server.application.*
import mu.KotlinLogging
import operators.CrawlingOperator
import operators.SearchOperator
import operators.requestOperator.RequestsOperator
import redis.clients.jedis.JedisPool
import storage.Frontier
import storage.RobotsStorage
import storage.VisitedURLs
import java.util.concurrent.locks.ReentrantLock

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.serverModule() {
    val jedis = JedisPool(HOST, PORT).resource
    val lock = ReentrantLock()

    val frontierRepository = FrontierRepository(lock, jedis)
    val frontierLogger = KotlinLogging.logger("Frontier")
    val frontier = Frontier(frontierRepository, frontierLogger)

    val visitedURLsRepository = VisitedURLsRepository(lock, jedis)
    val visitedURLs = VisitedURLs(visitedURLsRepository)

    val databaseConnection = SQLiteGateway().connect()
    val databaseLock = ReentrantLock()

    val robotsRepository = RobotsRepository(databaseLock, databaseConnection)
    val robotsLogger = KotlinLogging.logger("Hosts")
    val robotsStorage = RobotsStorage(robotsRepository, robotsLogger)

    val seoRepository = SEORepository(databaseLock, databaseConnection)
    val extractor = Extractor(seoRepository)

    val crawlingOperator = CrawlingOperator(frontier, visitedURLs, robotsStorage, extractor)
    val requestsOperator = RequestsOperator(frontier)
    val searchOperator = SearchOperator(seoRepository)

    routing(requestsOperator, searchOperator)
    crawlingOperator.run()
}
