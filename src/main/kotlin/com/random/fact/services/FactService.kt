package com.random.fact.services

import com.random.fact.client.OriginalFact
import com.random.fact.server.FactStatistics
import com.random.fact.server.OriginalURLFact
import com.random.fact.server.ShortenedURLFact
import com.random.fact.storage.FactStorage
import java.security.MessageDigest
import java.util.*
import kotlin.collections.List

class FactService(private val factStorage: FactStorage) {

    fun fetchShortenAndStore(fact: OriginalFact): ShortenedURLFact {
        val id = generateShortLink(fact.permalink)
        factStorage.getFact(id) ?: factStorage.storeFact(id, fact.text, fact.permalink)
        return ShortenedURLFact(fact.text, id)
    }

    fun getFactAndUpdateStatics(shortenedUrl: String): OriginalURLFact? {
        factStorage.incrementStatistics(shortenedUrl)
        return factStorage.getFact(shortenedUrl)?.let { OriginalURLFact(it.fact, it.permalink) }
    }

    fun getAllFacts(): List<OriginalURLFact> {
        return factStorage.getAllFacts().map { OriginalURLFact(it.fact, it.permalink) }
    }

    fun getAllStatistics(): List<FactStatistics> {
        return factStorage.getAllStatistics().map { FactStatistics(it.id, it.accessCount) }
    }

    fun generateShortLink(link: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(link.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes).take(8)
    }

}