package com.random.fact.server

import kotlinx.serialization.Serializable

@Serializable
data class ShortenedURLFact(val originalFact: String, val shortenedUrl: String)

@Serializable
data class OriginalURLFact(val fact: String, val originalPermalink: String)

@Serializable
data class FactStatistics(val shortenedUrl: String, val accessCount: Int)