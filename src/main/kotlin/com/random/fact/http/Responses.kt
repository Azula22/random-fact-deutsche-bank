package com.random.fact.http

import kotlinx.serialization.Serializable

@Serializable
data class OriginalFact(val text: String, val permalink: String)