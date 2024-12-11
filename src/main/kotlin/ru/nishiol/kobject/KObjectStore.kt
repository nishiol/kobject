package ru.nishiol.kobject

import ru.nishiol.kobject.KObject.Field
import ru.nishiol.kobject.KObject.Key

/**
 * Represents a store for managing key-value pairs in a [KObject].
 *
 * **Implementation Note:**
 * Implementations of [KObjectStore] must ensure that keys are looked up using their [Key.equals] and [Key.hashCode] functions.
 * This ensures consistent behavior across different store implementations.
 */
interface KObjectStore {
    /**
     * The set of keys in this store.
     */
    val keys: Set<Key<Any?>>

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key The key to retrieve the value for.
     * @return The value associated with the key, or `null` if not present.
     */
    fun get(key: Key<Any?>): Any?

    /**
     * Retrieves all values associated with the specified keys.
     *
     * @param keys The keys to retrieve values for.
     * @return A map containing the key-value pairs.
     */
    fun getAll(keys: Set<Key<Any?>>): Map<Key<Any?>, Any?> {
        val fields = mutableMapOf<Key<Any?>, Any?>()
        val existingKeys = existingKeys(keys)
        for (key in existingKeys) {
            fields += key to get(key)
        }
        return fields
    }

    /**
     * Checks if this store contains the specified key.
     *
     * @param key The key to check for.
     * @return `true` if the key exists, `false` otherwise.
     */
    fun contains(key: Key<Any?>): Boolean

    /**
     * Retrieves the subset of keys that exist in this store.
     *
     * @param keys The keys to check for.
     * @return A set of keys that are present in this store.
     */
    fun existingKeys(keys: Set<Key<Any?>>): Set<Key<Any?>> {
        val existingKeys = mutableSetOf<Key<Any?>>()
        for (key in keys) {
            if (contains(key)) {
                existingKeys += key
            }
        }
        return existingKeys
    }

    /**
     * Creates a new store with the specified field added or updated.
     *
     * @param field The field to add or update.
     * @return A new store with the updated field.
     */
    fun with(field: Field<Any?>): KObjectStore = withAll(listOf(field))

    /**
     * Creates a new store with the specified fields added or updated.
     *
     * @param fields The fields to add or update.
     * @return A new store with the updated fields.
     */
    fun withAll(fields: Iterable<Field<Any?>>): KObjectStore

    /**
     * Creates a new store with the specified key removed.
     *
     * @param key The key to remove.
     * @return A new store without the specified key.
     */
    fun without(key: Key<Any?>): KObjectStore = withoutAll(setOf(key))

    /**
     * Creates a new store with the specified keys removed.
     *
     * @param keys The keys to remove.
     * @return A new store without the specified keys.
     */
    fun withoutAll(keys: Set<Key<Any?>>): KObjectStore
}

/**
 * A mutable version of [KObjectStore], allowing for modifications.
 */
interface MutableKObjectStore : KObjectStore {
    /**
     * Associates the specified value with the specified key.
     *
     * @param key The key to associate the value with.
     * @param value The value to associate with the key.
     */
    fun put(key: Key<Any?>, value: Any?)

    /**
     * Associates all the specified fields with their keys.
     *
     * @param fields The fields to associate.
     */
    fun putAll(fields: Iterable<Field<Any?>>) {
        for (entry in fields) {
            put(entry.key, entry.value)
        }
    }

    /**
     * Removes the value associated with the specified key.
     *
     * @param key The key to remove the value for.
     */
    fun remove(key: Key<Any?>)

    /**
     * Removes the values associated with the specified keys.
     *
     * @param keys The keys to remove values for.
     */
    fun removeAll(keys: Set<Key<Any?>>) {
        for (key in keys) {
            remove(key)
        }
    }

    /**
     * Computes a value if absent for the specified key and associates it.
     *
     * @param key The key to compute the value for if absent.
     * @param compute The function to compute the value.
     * @return The existing or newly computed value.
     */
    fun computeIfAbsent(key: Key<Any?>, compute: () -> Any?): Any? {
        var result = get(key)
        if (result == null) {
            result = compute()
            if (result != null) {
                put(key, result)
            }
        }
        return result
    }

    override fun with(field: Field<Any?>): MutableKObjectStore = withAll(listOf(field))

    override fun withAll(fields: Iterable<Field<Any?>>): MutableKObjectStore

    override fun without(key: Key<Any?>): MutableKObjectStore = withoutAll(setOf(key))

    override fun withoutAll(keys: Set<Key<Any?>>): MutableKObjectStore
}
