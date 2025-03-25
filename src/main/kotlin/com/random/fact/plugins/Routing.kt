package com.random.fact.plugins

import com.random.fact.http.OriginalFactsClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(client: OriginalFactsClient) {
    routing {
        post("/facts") {
            val fact = client.getFact()
            call.respond(HttpStatusCode.OK, fact!!)
        }
    }
}