import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*

import operators.ApplicationStartupOperator
import server.serverModule

fun main(args: Array<String>) {
    ApplicationStartupOperator().setupArgs(args)
    embeddedServer(CIO, port = 8080, module = Application::serverModule, configure = {connectionIdleTimeoutSeconds = 15}).start(wait = true)
}