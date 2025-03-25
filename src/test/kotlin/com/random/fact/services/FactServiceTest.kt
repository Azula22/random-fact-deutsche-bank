package com.random.fact.services

import com.random.fact.client.OriginalFact
import com.random.fact.storage.FactStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FactServiceTest {

    @Test
    fun `fetchAndShorten should store a new fact and return shortened URL`() {

        val factService = FactService(FactStorage())
        val fact = OriginalFact("Interesting fact", "https://example.com")

        val shortenedFact = factService.fetchAndShorten(fact)

        assertEquals("Interesting fact", shortenedFact.originalFact)
        assertEquals(factService.generateShortLink(fact.permalink), shortenedFact.shortenedUrl)

        val storedFact = factService.getFactAndUpdateStatics(shortenedFact.shortenedUrl)
        assertNotNull(storedFact, "Fact should be stored in FactStorage")
        assertEquals(fact.text, storedFact.fact)
        assertEquals(fact.permalink, storedFact.originalPermalink)
    }

    @Test
    fun `fetchAndShorten should return existing shortened URL for duplicate facts`() {
        val factService = FactService(FactStorage())

        val fact = OriginalFact("Repeated fact", "https://repeated.com")

        val firstShortened = factService.fetchAndShorten(fact)
        val secondShortened = factService.fetchAndShorten(fact)

        assertEquals(firstShortened.shortenedUrl, secondShortened.shortenedUrl)
        assertEquals(1, factService.getAllFacts().size, "Storage should not create duplicate entries")
        assertEquals(0, factService.getAllStatistics().first().accessCount, "Statistics should be ")
    }

    @Test
    fun `getFactAndUpdateStatistics should return fact and update access count`() {
        val factService = FactService(FactStorage())
        val fact = OriginalFact("Access test fact", "https://access-test.com")
        val shortenedFact = factService.fetchAndShorten(fact)

        val retrievedFact = factService.getFactAndUpdateStatics(shortenedFact.shortenedUrl)

        assertNotNull(retrievedFact)
        assertEquals(fact.text, retrievedFact.fact)
        assertEquals(fact.permalink, retrievedFact.originalPermalink)

        val count = factService.getAllStatistics().find { it.shortenedUrl == shortenedFact.shortenedUrl }?.accessCount
        assertEquals(1, count, "Access count should be incremented")
    }

    @Test
    fun `getFactAndUpdateStatistics should return null for non-existent short URL`() {
        val factService = FactService(FactStorage())
        val retrievedFact = factService.getFactAndUpdateStatics("nonexistent123")
        assertNull(retrievedFact, "Should return null for non-existent fact")
    }

    @Test
    fun `getAllFacts should return all stored facts`() {
        val factService = FactService(FactStorage())
        factService.fetchAndShorten(OriginalFact("Fact A", "https://facta.com"))
        factService.fetchAndShorten(OriginalFact("Fact B", "https://factb.com"))

        val allFacts = factService.getAllFacts()

        assertEquals(2, allFacts.size)
        assertTrue(allFacts.any { it.fact == "Fact A" })
        assertTrue(allFacts.any { it.fact == "Fact B" })
    }

    @Test
    fun `getAllFacts should return empty list when no facts are stored`() {
        val factService = FactService(FactStorage())
        val allFacts = factService.getAllFacts()
        assertTrue(allFacts.isEmpty(), "Should return empty list if no facts exist")
    }

    @Test
    fun `getAllStatistics should return correct access counts`() {
        val factService = FactService(FactStorage())
        val fact1 = factService.fetchAndShorten(OriginalFact("Fact 1", "https://fact1.com"))
        val fact2 = factService.fetchAndShorten(OriginalFact("Fact 2", "https://fact2.com"))

        factService.getFactAndUpdateStatics(fact1.shortenedUrl)
        factService.getFactAndUpdateStatics(fact1.shortenedUrl)
        factService.getFactAndUpdateStatics(fact2.shortenedUrl)

        val stats = factService.getAllStatistics()
        assertEquals(2, stats.size)
        assertEquals(2, stats.find { it.shortenedUrl == fact1.shortenedUrl }?.accessCount)
        assertEquals(1, stats.find { it.shortenedUrl == fact2.shortenedUrl }?.accessCount)
    }

    @Test
    fun `getAllStatistics should return empty list when 0 facts are saved`() {
        val factService = FactService(FactStorage())
        val stats = factService.getAllStatistics()
        assertTrue(stats.isEmpty(), "Should return empty statistics list when 0 facts are saved")
    }
}