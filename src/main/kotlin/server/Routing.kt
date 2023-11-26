package server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import operators.requestOperator.RequestsOperator

fun Application.routing(requestsOperator: RequestsOperator) {
    routing {
        route("/crawling") {
            post("request"){
                val request = call.receiveText()
                call.respond(HttpStatusCode.OK)
                requestsOperator.handleRequest(request)
            }
        }
        route("/health") {
            get(){
                call.respond("I'm alive!")
            }
        }
    }
}