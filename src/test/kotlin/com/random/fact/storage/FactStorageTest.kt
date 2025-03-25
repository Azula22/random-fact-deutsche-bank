package com.random.fact.storage

import kotlin.test.*

class FactStorageTest {

    @Test
    fun `storeFact should add a new fact to storage`() {
        val factStorage = FactStorage()

        factStorage.storeFact("abc123", "Test Fact", "https://example.com")

        val storedFact = factStorage.getFact("abc123")
        assertNotNull(storedFact, "Fact should be stored")
        assertEquals("Test Fact", storedFact.fact)
        assertEquals("https://example.com", storedFact.permalink)
        assertEquals(0, storedFact.accessCount.get(), "Access count should be initialized to 0")
    }

    @Test
    fun `incrementStatistics should increase access count`() {
        val factStorage = FactStorage()
        factStorage.storeFact("xyz789", "Another Fact", "https://example.org")

        val before = factStorage.getFact("xyz789")?.accessCount?.get() ?: -1
        factStorage.incrementStatistics("xyz789")
        val after = factStorage.getFact("xyz789")?.accessCount?.get()

        assertEquals(before + 1, after, "Access count should increment by 1")
    }

    @Test
    fun `getFact should return correct fact details`() {
        val factStorage = FactStorage()
        factStorage.storeFact("test456", "Sample Fact", "https://sample.com")

        val fact = factStorage.getFact("test456")
        assertNotNull(fact)
        assertEquals("Sample Fact", fact.fact)
        assertEquals("https://sample.com", fact.permalink)
    }

    @Test
    fun `getAllFacts should return all stored facts`() {
        val factStorage = FactStorage()
        factStorage.storeFact("id1", "Fact 1", "https://link1.com")
        factStorage.storeFact("id2", "Fact 2", "https://link2.com")

        val allFacts = factStorage.getAllFacts()
        assertEquals(2, allFacts.size, "Should return all stored facts")
        assertTrue(allFacts.any { it.fact == "Fact 1" })
        assertTrue(allFacts.any { it.fact == "Fact 2" })
    }

    @Test
    fun `getAllStatistics should return correct statistics`() {
        val factStorage = FactStorage()
        factStorage.storeFact("stat1", "Fact A", "https://facta.com")
        factStorage.storeFact("stat2", "Fact B", "https://factb.com")

        factStorage.incrementStatistics("stat1")
        factStorage.incrementStatistics("stat1")
        factStorage.incrementStatistics("stat2")

        val stats = factStorage.getAllStatistics()
        assertEquals(2, stats.size)
        assertEquals(2, stats.find { it.id == "stat1" }?.accessCount)
        assertEquals(1, stats.find { it.id == "stat2" }?.accessCount)
    }

    @Test
    fun `getFact should return null for non-existent short URL`() {
        val factStorage = FactStorage()
        val fact = factStorage.getFact("nonexistent123")
        assertNull(fact, "Should return null for a non-existent fact")
    }

    @Test
    fun `getAllStatistics should return empty list when no facts are stored`() {
        val factStorage = FactStorage()
        val stats = factStorage.getAllStatistics()
        assertTrue(stats.isEmpty(), "Statistics should be empty when no facts are stored")
    }

    @Test
    fun `getAllFacts should return empty list when no facts are stored`() {
        val factStorage = FactStorage()
        val facts = factStorage.getAllFacts()
        assertTrue(facts.isEmpty(), "Fact list should be empty when no facts are stored")
    }

}