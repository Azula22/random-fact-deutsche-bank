package com.random.fact

import com.random.fact.client.OriginalFactsClient
import com.random.fact.plugins.configureRouting
import com.random.fact.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val uselessFactsUrl = environment.config.property("useless_facts_url").getString()
    val originalFactsClient = OriginalFactsClient(uselessFactsUrl)

    configureRouting(originalFactsClient)
    configureSerialization()
}
