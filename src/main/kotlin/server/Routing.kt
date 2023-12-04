package server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import operators.SearchOperator
import operators.requestOperator.RequestsOperator

fun Application.routing(requestsOperator: RequestsOperator, searchOperator: SearchOperator) {
    install(ContentNegotiation) {
        json()
    }
    install(CORS){
        allowHost("*")
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        route("/crawling") {
            post("request"){
                val request = call.receiveText()
                call.respond(HttpStatusCode.OK)
                requestsOperator.handleRequest(request)
            }
        }
        route("/search"){
            get("request"){
                val query = call.request.queryParameters["q"]
                if(query == null){
                    call.respond("Damn man you are a teapot")
                } else{
                    call.respond(searchOperator.searchAndSortByPriority(query))
                }
            }
        }
    }
}