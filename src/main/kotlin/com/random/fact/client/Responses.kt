package com.random.fact.client

import kotlinx.serialization.Serializable

@Serializable
data class OriginalFact(val text: String, val permalink: String)