package ru.nishiol.kobject

import kotlin.test.*

class MutableKObjectTests {

    @Test
    fun `put adds new key-value pair`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf()

        // When
        mutableKObject[key] = "value"

        // Then
        assertTrue(key in mutableKObject)
        assertEquals("value", mutableKObject[key])
    }

    @Test
    fun `put updates value for existing key`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf(KObject.Field(key, "initialValue"))

        // When
        mutableKObject[key] = "updatedValue"

        // Then
        assertEquals("updatedValue", mutableKObject[key])
    }

    @Test
    fun `remove deletes a key-value pair`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf(KObject.Field(key, "value"))

        // When
        mutableKObject -= key

        // Then
        assertFalse(key in mutableKObject)
        assertNull(mutableKObject[key])
    }

    @Test
    fun `remove does nothing for non-existing key`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf()

        // When
        mutableKObject -= key

        // Then
        assertFalse(key in mutableKObject)
        assertNull(mutableKObject[key])
    }

    @Test
    fun `plus adds fields from another KObject`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val mutableKObject = mutableKObjectOf(KObject.Field(key1, "value1"))
        val anotherKObject = kObjectOf(KObject.Field(key2, 42))

        // When
        mutableKObject += anotherKObject

        // Then
        assertTrue(key1 in mutableKObject)
        assertTrue(key2 in mutableKObject)
        assertEquals("value1", mutableKObject[key1])
        assertEquals(42, mutableKObject[key2])
    }

    @Test
    fun `minus removes keys from another KObject`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val mutableKObject = mutableKObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )
        val anotherKObject = kObjectOf(KObject.Field(key2, 42))

        // When
        mutableKObject -= anotherKObject

        // Then
        assertTrue(key1 in mutableKObject)
        assertFalse(key2 in mutableKObject)
        assertNull(mutableKObject[key2])
    }

    @Test
    fun `keys reflects changes after modification`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val mutableKObject = mutableKObjectOf(KObject.Field(key1, "value1"))

        // When
        mutableKObject[key2] = 42
        mutableKObject -= key1

        // Then
        assertFalse(key1 in mutableKObject)
        assertTrue(key2 in mutableKObject)
        assertEquals(setOf(key2), mutableKObject.keys)
    }

    @Test
    fun `put handles null value`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf()

        // When
        mutableKObject[key] = null

        // Then
        assertTrue(key in mutableKObject)
        assertNull(mutableKObject[key])
    }

    @Test
    fun `plus with multiple fields adds all to KObject`() {
        // Given`
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val mutableKObject = mutableKObjectOf()

        // When
        mutableKObject += listOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )

        // Then
        assertTrue(key1 in mutableKObject)
        assertTrue(key2 in mutableKObject)
        assertEquals("value1", mutableKObject[key1])
        assertEquals(42, mutableKObject[key2])
    }

    @Test
    fun `minus with multiple keys removes all from KObject`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val mutableKObject = mutableKObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )

        // When
        mutableKObject -= setOf(key1, key2)

        // Then
        assertTrue(mutableKObject.keys.isEmpty())
        assertFalse(key1 in mutableKObject)
        assertFalse(key2 in mutableKObject)
    }

    @Test
    fun `computeIfAbsent calculates and stores value if absent`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf()

        // When
        val value = mutableKObject.computeIfAbsent(key) { "calculatedValue" }

        // Then
        assertEquals("calculatedValue", value)
        assertEquals("calculatedValue", mutableKObject[key])
    }

    @Test
    fun `computeIfAbsent does not overwrite existing value`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf(KObject.Field(key, "existingValue"))

        // When
        val value = mutableKObject.computeIfAbsent(key) { "newValue" }

        // Then
        assertEquals("existingValue", value)
        assertEquals("existingValue", mutableKObject[key])
    }
}