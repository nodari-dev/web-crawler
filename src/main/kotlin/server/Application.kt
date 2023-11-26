package server

import configuration.Configuration.HOST
import configuration.Configuration.PORT
import infrastructure.repository.FrontierRepository
import infrastructure.repository.HostsRepository
import infrastructure.repository.VisitedURLsRepository
import io.ktor.server.application.*
import mu.KotlinLogging
import operators.CrawlingOperator
import operators.requestOperator.RequestsOperator
import redis.clients.jedis.JedisPool
import storage.Frontier
import storage.HostsStorage
import storage.VisitedURLs
import java.util.concurrent.locks.ReentrantLock

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.serverModule() {
    val lock = ReentrantLock()
    val jedis = JedisPool(HOST, PORT).resource

    val frontierRepository = FrontierRepository(lock, jedis)
    val frontierLogger = KotlinLogging.logger("Frontier")
    val frontier = Frontier(frontierRepository, frontierLogger)

    val visitedURLsRepository = VisitedURLsRepository(lock, jedis)
    val visitedURLs = VisitedURLs(visitedURLsRepository)

    val hostsRepository = HostsRepository(lock, jedis)
    val hostsLogger = KotlinLogging.logger("Hosts")
    val hostsStorage = HostsStorage(hostsRepository, hostsLogger)

    val crawlingOperator = CrawlingOperator(frontier, visitedURLs, hostsStorage)
    val requestsOperator = RequestsOperator(frontier)
    routing(requestsOperator)
    crawlingOperator.run()
}
