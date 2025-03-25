package com.random.fact.services

import com.random.fact.client.OriginalFact
import com.random.fact.server.FactStatistics
import com.random.fact.server.OriginalURLFact
import com.random.fact.server.ShortenedURLFact
import com.random.fact.storage.FactStorage
import com.random.fact.storage.FactStorage.incrementStatistics
import com.random.fact.storage.FactStorage.storeFact
import java.security.MessageDigest
import java.util.*
import kotlin.collections.List

object FactService {

    fun fetchAndShorten(fact: OriginalFact): ShortenedURLFact {
        val id = generateId(fact.permalink)
        FactStorage.getFact(id) ?: storeFact(id, fact.text, fact.permalink)
        return ShortenedURLFact(fact.text, id)
    }

    fun getFactAndUpdateStatics(shortenedUrl: String): OriginalURLFact? {
        incrementStatistics(shortenedUrl)
        return FactStorage.getFact(shortenedUrl)?.let { OriginalURLFact(it.fact, it.permalink) }
    }

    fun getAllFacts(): List<OriginalURLFact> {
        return FactStorage.getAllFacts().map { OriginalURLFact(it.fact, it.permalink) }
    }

    fun getAllStatistics(): List<FactStatistics> {
        return FactStorage.getAllStatistics().map { FactStatistics(it.id, it.accessCount) }
    }

    private fun generateId(link: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(link.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).take(8)
    }

}