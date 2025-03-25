package com.random.fact

import com.random.fact.http.OriginalFactsClient
import com.random.fact.plugins.configureRouting
import com.random.fact.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val uselessFactUrl = environment.config.property("useless_fact_url").getString()
    val originalFactsClient = OriginalFactsClient(uselessFactUrl)

    configureRouting(originalFactsClient)
    configureSerialization()
}
