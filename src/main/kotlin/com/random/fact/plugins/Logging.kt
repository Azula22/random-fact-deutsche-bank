package com.random.fact.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import org.slf4j.event.Level

fun Application.configureRequestLogging() {
    install(CallLogging){
        level = Level.INFO
    }
}