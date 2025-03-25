package com.random.fact.plugins

import com.random.fact.client.OriginalFactsClient
import com.random.fact.services.FactService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(client: OriginalFactsClient) {
    routing {

        post("/facts") {
            client.getFact()
                ?.let {
                    val result = FactService.fetchAndShorten(it)
                    call.respond(HttpStatusCode.OK, result)
                }
                ?: call.respond(HttpStatusCode.ServiceUnavailable)

        }

        get("/facts/{shortenedUrl}") {
            call.parameters["shortenedUrl"]?.let { shortenedURL ->
                FactService.getFactAndUpdateStatics(shortenedURL)?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(HttpStatusCode.NotFound)
            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        get("/facts") {
            call.respond(HttpStatusCode.OK, FactService.getAllFacts())
        }

        get("/facts/{shortenedUrl}/redirect") {
            call.parameters["shortenedUrl"]?.let { shortenedURL ->
                FactService.getFactAndUpdateStatics(shortenedURL)?.let {
                    call.respondRedirect(it.originalPermalink, true)
                }

            } ?: call.respond(HttpStatusCode.BadRequest)
        }

        get("/admin/statistics") {
            call.respond(HttpStatusCode.OK, FactService.getAllStatistics())
        }
    }
}

