package com.random.fact.storage

import com.random.fact.storage.models.FactDetails
import com.random.fact.storage.models.Statistics
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object FactStorage {
    private val storage = ConcurrentHashMap<String, FactDetails>()

    fun storeFact(shortUrl: String, text: String, permalink: String) {
        storage[shortUrl] = FactDetails(text, permalink, AtomicInteger(0))
    }

    fun incrementStatistics(shortUrl: String) =
        storage[shortUrl]?.accessCount?.incrementAndGet()

    fun getFact(shortUrl: String): FactDetails? = storage[shortUrl]

    fun getAllFacts(): List<FactDetails> = storage.values.toList()

    fun getAllStatistics(): List<Statistics> = storage.map { Statistics(it.key, it.value.accessCount.get()) }

}