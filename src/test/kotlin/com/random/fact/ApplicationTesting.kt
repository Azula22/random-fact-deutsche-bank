package com.random.fact

import com.random.fact.client.OriginalFact
import com.random.fact.client.OriginalFactsClient
import com.random.fact.plugins.configureRouting
import com.random.fact.plugins.configureSerialization
import com.random.fact.services.FactService
import com.random.fact.storage.FactStorage
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ApplicationTesting {

    @Test
    fun fetchFactAndGetStatistics() = execute { client ->
        val fetchFactCall = client.post("/facts")
        assertEquals(HttpStatusCode.OK, fetchFactCall.status)
        assertEquals(
            Json.parseToJsonElement("""{"originalFact":"Random!","shortenedUrl":"5DM3yzUf"}"""),
            Json.parseToJsonElement(fetchFactCall.bodyAsText())
        )
        val getStatistics = client.get("/admin/statistics")
        assertEquals(HttpStatusCode.OK, getStatistics.status)
        assertEquals(
            Json.parseToJsonElement("""[{"shortenedUrl":"5DM3yzUf","accessCount":0}]"""),
            Json.parseToJsonElement(getStatistics.bodyAsText())
        )
    }

    @Test
    fun getFactByShortLink() = execute { client ->
        client.post("/facts")
        val getFact = client.get("/facts/5DM3yzUf")
        assertEquals(HttpStatusCode.OK, getFact.status)
        assertEquals(
            Json.parseToJsonElement("""{"fact":"Random!","originalPermalink":"https://test.com/1122aabb"}"""),
            Json.parseToJsonElement(getFact.bodyAsText())
        )
    }

    @Test
    fun getFactByShortLinkAndCheckStatistics() = execute { client ->
        client.post("/facts")
        val getFact = client.get("/facts/5DM3yzUf")
        assertEquals(HttpStatusCode.OK, getFact.status)
        assertEquals(
            Json.parseToJsonElement("""{"fact":"Random!","originalPermalink":"https://test.com/1122aabb"}"""),
            Json.parseToJsonElement(getFact.bodyAsText())
        )
        val getStatistics = client.get("/admin/statistics")
        assertEquals(HttpStatusCode.OK, getStatistics.status)
        assertEquals(
            Json.parseToJsonElement("""[{"shortenedUrl":"5DM3yzUf","accessCount":1}]"""),
            Json.parseToJsonElement(getStatistics.bodyAsText())
        )
    }

    @Test
    fun getAllFacts() = execute { client ->
        client.post("/facts")
        val getStatistics = client.get("/facts")
        assertEquals(HttpStatusCode.OK, getStatistics.status)
        assertEquals(
            Json.parseToJsonElement("""[{"fact":"Random!","originalPermalink":"https://test.com/1122aabb"}]"""),
            Json.parseToJsonElement(getStatistics.bodyAsText())
        )
    }

    @Test
    fun redirectFact() = execute { client ->
        client.post("/facts")
        val redirected = client.get("/facts/5DM3yzUf/redirect")
        assertEquals(HttpStatusCode.OK, redirected.status)
        assertEquals(
            Json.parseToJsonElement("""{"id":"112233","text":"Random!","source":"djtech.net","source_url":"http://www.whocares.net.htm","language":"en","permalink":"https://test.com/1122aabb"}"""),
            Json.parseToJsonElement(redirected.bodyAsText())
        )
    }

    @Test
    fun factDoesNotExist() = execute { client ->
        val result = client.get("/facts/none")
        val getStatistics = client.get("/facts")

        assertEquals(HttpStatusCode.NotFound, result.status)
        assertEquals(HttpStatusCode.OK, getStatistics.status)
        assertEquals(
            Json.parseToJsonElement("[]"),
            Json.parseToJsonElement(getStatistics.bodyAsText())
        )
    }

    @Test
    fun wrongRequest() = execute { client ->
        val result = client.get("/lala")
        assertEquals(HttpStatusCode.NotFound, result.status)
    }

    private fun execute(test: suspend (HttpClient) -> Unit) = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        }
        application {
            val originalFactsClient = OriginalFactsClient("https://test.com", client)
            val factService = FactService(FactStorage())
            configureRouting(originalFactsClient, factService)
            configureSerialization()
        }
        externalServices {
            hosts("https://test.com") {
                install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                    json()
                }
                routing {
                    get("/") {
                        call.respond(OriginalFact("Random!", "https://test.com/1122aabb"))
                    }
                    get("/1122aabb") {
                        call.respondText { """{"id":"112233","text":"Random!","source":"djtech.net","source_url":"http://www.whocares.net.htm","language":"en","permalink":"https://test.com/1122aabb"}""" }
                    }
                }
            }
        }
        test(client)
    }

}