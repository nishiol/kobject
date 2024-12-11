package ru.nishiol.kobject

import kotlin.test.*

class KObjectTests {
    @Test
    fun `get returns value for existing key`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf(KObject.Field(key, "value"))

        // When
        val result = kObject[key]

        // Then
        assertEquals("value", result)
    }

    @Test
    fun `get returns null for non-existing key`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf()
    
        // When
        val result = kObject[key]

        // Then
        assertNull(result)
    }

    @Test
    fun `contains returns true for existing key`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf(KObject.Field(key, "value"))

        // When
        val result = key in kObject

        // Then
        assertTrue(result)
    }

    @Test
    fun `contains returns false for non-existing key`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf()

        // When
        val result = key in kObject

        // Then
        assertFalse(result)
    }

    @Test
    fun `keys returns all keys in kObject`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val kObject = kObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )

        // When
        val result = kObject.keys

        // Then
        assertEquals(setOf(key1, key2), result)
    }

    @Test
    fun `plus adds entry to kObject`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf()

        // When
        val updatedKObject = kObject + KObject.Field(key, "value")

        // Then
        assertTrue(key in updatedKObject)
        assertEquals("value", updatedKObject[key])
    }

    @Test
    fun `minus removes key from kObject`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf(KObject.Field(key, "value"))

        // When
        val updatedKObject = kObject - key

        // Then
        assertFalse(key in updatedKObject)
        assertNull(updatedKObject[key])
    }

    @Test
    fun `adding entry does not modify original kObject`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf(KObject.Field(key, "value"))

        // When
        val updatedKObject = kObject + KObject.Field(key, "newValue")

        // Then
        assertEquals("value", kObject[key])
        assertEquals("newValue", updatedKObject[key])
    }

    @Test
    fun `merging kObjects combines keys and values`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val kObject1 = kObjectOf(KObject.Field(key1, "value1"))
        val kObject2 = kObjectOf(KObject.Field(key2, 42))

        // When
        val mergedKObject = kObject1 + kObject2

        // Then
        assertTrue(key1 in mergedKObject)
        assertTrue(key2 in mergedKObject)
        assertEquals("value1", mergedKObject[key1])
        assertEquals(42, mergedKObject[key2])
    }

    @Test
    fun `merging kObjects overwrites existing keys`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject1 = kObjectOf(KObject.Field(key, "value1"))
        val kObject2 = kObjectOf(KObject.Field(key, "value2"))

        // When
        val mergedKObject = kObject1 + kObject2

        // Then
        assertEquals("value2", mergedKObject[key])
    }

    @Test
    fun `minus with multiple keys removes them from kObject`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val kObject = kObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )

        // When
        val updatedKObject = kObject - setOf(key1, key2)

        // Then
        assertFalse(key1 in updatedKObject)
        assertFalse(key2 in updatedKObject)
        assertTrue(updatedKObject.keys.isEmpty())
    }

    @Test
    fun `plus with multiple fields adds them from kObject`() {
        // Given
        val entry1 = KObject.Field(object : KObject.Key<String> {}, "value2")
        val entry2 = KObject.Field(object : KObject.Key<Int> {}, 43)

        val KObject = kObjectOf(
            KObject.Field(object : KObject.Key<String> {}, "value1"),
            KObject.Field(object : KObject.Key<Int> {}, 42)
        )

        // When
        val updatedKObject = KObject + listOf(entry1, entry2)

        // Then
        assertTrue(entry1.key in updatedKObject)
        assertEquals(entry1.value, updatedKObject[entry1.key])
        assertTrue(entry2.key in updatedKObject)
        assertEquals(entry2.value, updatedKObject[entry2.key])
    }

    @Test
    fun `toMutableKObject creates independent mutable kObject`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf(KObject.Field(key, "value"))

        // When
        val mutableKObject = kObject.toMutableKObject()
        mutableKObject[key] = "newValue"

        // Then
        assertEquals("value", kObject[key])
        assertEquals("newValue", mutableKObject[key])
    }

    @Test
    fun `removing non-existing key does nothing`() {
        // Given
        val key = object : KObject.Key<String> {}
        val kObject = kObjectOf()

        // When
        val updatedKObject = kObject - key

        // Then
        assertEquals(kObject.keys, updatedKObject.keys)
    }

    @Test
    fun `getAll returns only existing keys`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val key3 = object : KObject.Key<Double> {}
        val kObject = kObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )

        // When
        val result = kObject.getAll(setOf(key1, key2, key3))

        // Then
        assertEquals(setOf(key1, key2), result.keys)
        assertEquals("value1", result[key1])
        assertEquals(42, result[key2])
        assertNull(result[key3])
    }

    @Test
    fun `toKObject creates independent immutable kObject`() {
        // Given
        val key = object : KObject.Key<String> {}
        val mutableKObject = mutableKObjectOf(KObject.Field(key, "value"))

        // When
        val immutableKObject = mutableKObject.toKObject()
        mutableKObject[key] = "newValue"

        // Then
        assertEquals("value", immutableKObject[key])
        assertEquals("newValue", mutableKObject[key])
    }

    @Test
    fun `minus kObject removes all keys from another kObject`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val key3 = object : KObject.Key<Double> {}

        val kObject1 = kObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )
        val kObject2 = kObjectOf(
            KObject.Field(key2, 43),
            KObject.Field(key3, 3.14)
        )

        // When
        val resultKObject = kObject1 - kObject2

        // Then
        assertTrue(key1 in resultKObject)
        assertFalse(key2 in resultKObject)
        assertFalse(key3 in resultKObject)

        assertEquals("value1", resultKObject[key1])
        assertNull(resultKObject[key2])
        assertNull(resultKObject[key3])
    }

    @Test
    fun `minus kObject does nothing if no keys overlap`() {
        // Given
        val key1 = object : KObject.Key<String> {}
        val key2 = object : KObject.Key<Int> {}
        val key3 = object : KObject.Key<Double> {}

        val kObject1 = kObjectOf(
            KObject.Field(key1, "value1"),
            KObject.Field(key2, 42)
        )
        val kObject2 = kObjectOf(
            KObject.Field(key3, 3.14)
        )

        // When
        val resultKObject = kObject1 - kObject2

        // Then
        assertTrue(key1 in resultKObject)
        assertTrue(key2 in resultKObject)
        assertFalse(key3 in resultKObject)

        assertEquals("value1", resultKObject[key1])
        assertEquals(42, resultKObject[key2])
        assertNull(resultKObject[key3])
    }
}