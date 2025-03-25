package com.random.fact.storage.models

import java.util.concurrent.atomic.AtomicInteger

data class FactDetails(val fact: String, val permalink: String, var accessCount: AtomicInteger)

data class Statistics(val id: String, var accessCount: Int)