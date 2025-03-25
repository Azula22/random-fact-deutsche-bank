package com.random.fact.http

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.*
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OriginalFactsClient(private val url: String) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging)
    }

    suspend fun getFact(): OriginalFact? {
        val response = client.get(url)
        return if (response.status != HttpStatusCode.OK) {
            logger.warn(
                "Unexpected response from original fact client: " +
                        "status ${response.status}, body ${response.bodyAsText()}"
            )
            null
        } else {
            response.body<OriginalFact>()
        }
    }

}