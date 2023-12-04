import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import operators.ApplicationStartupOperator
import server.serverModule

fun main(args: Array<String>) {
    ApplicationStartupOperator().setupArgs(args)
    embeddedServer(Netty, port = 8080, module = Application::serverModule).start(wait = true)
}