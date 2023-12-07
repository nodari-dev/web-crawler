package server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*

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
                if(request.isEmpty()){
                    call.respond(HttpStatusCode.BadRequest)
                } else{
                    requestsOperator.handleRequest(request)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
        route("/search"){
            get("request"){
                val searchQuery = call.request.queryParameters["q"]
                val page = call.request.queryParameters["page"]
                val size = call.request.queryParameters["size"]
                if(searchQuery.isNullOrEmpty() || page == null || size == null){
                    call.respond("Damn man you're a teapot")
                } else{
                    call.respond(searchOperator.searchAndSortByPriority(searchQuery, page.toInt(), size.toInt()))
                }
            }
        }
    }
}