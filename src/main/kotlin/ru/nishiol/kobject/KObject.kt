package ru.nishiol.kobject

import ru.nishiol.kobject.KObject.Field
import ru.nishiol.kobject.KObject.Key

/**
 * Represents a key-value object with type-safe access and manipulation.
 * Provides functionality for working with immutable key-value pairs.
 */
interface KObject {

    /**
     * Represents a key for accessing a value in the [KObject].
     *
     * **Implementation Note:**
     * Implementors of [Key] must correctly override [Any.equals] and [Any.hashCode],
     * or use a single class instance per key to ensure proper functionality when used
     * as part of a [KObject]. Failure to do so may result in unexpected behavior.
     *
     * @param V The type of the value associated with this key.
     */
    interface Key<out V>

    /**
     * Represents a collection of key-value pairs.
     *
     * @property fields The map of keys to their associated values.
     */
    data class Fields(private val fields: Map<Key<*>, *>) {

        /**
         * The set of keys in this collection.
         */
        val keys: Set<Key<*>> = fields.keys

        /**
         * Retrieves the value associated with the specified key.
         *
         * @param key The key to retrieve the value for.
         * @return The value associated with the key, or `null` if not present.
         */
        @Suppress("UNCHECKED_CAST")
        operator fun <V> get(key: Key<V>): V? = fields[key] as? V

        /**
         * Checks if the collection contains the specified key.
         *
         * @param key The key to check for.
         * @return `true` if the key exists, `false` otherwise.
         */
        operator fun contains(key: Key<*>): Boolean = fields.containsKey(key)
    }

    /**
     * Represents a single key-value pair.
     *
     * @param V The type of the value associated with this key.
     * @property key The key for this field.
     * @property value The value associated with this key.
     */
    data class Field<out V>(val key: Key<V>, val value: V)

    /**
     * The set of keys in this [KObject].
     */
    val keys: Set<Key<*>>

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key The key to retrieve the value for.
     * @return The value associated with the key, or `null` if not present.
     */
    fun <V> get(key: Key<V>): V?

    /**
     * Retrieves all values associated with the specified keys.
     *
     * @param keys The keys to retrieve values for.
     * @return A `Fields` object containing the key-value pairs.
     */
    fun getAll(keys: Set<Key<*>>): Fields

    /**
     * Checks if this [KObject] contains the specified key.
     *
     * @param key The key to check for.
     * @return `true` if the key exists, `false` otherwise.
     */
    fun contains(key: Key<*>): Boolean

    /**
     * Retrieves the subset of keys that exist in this [KObject].
     *
     * @param keys The keys to check for.
     * @return A set of keys that are present in this [KObject].
     */
    fun existingKeys(keys: Set<Key<*>>): Set<Key<*>>

    /**
     * Creates a new [KObject] with the specified field added or updated.
     *
     * @param field The field to add or update.
     * @return A new [KObject] with the updated field.
     */
    fun with(field: Field<*>): KObject

    /**
     * Creates a new [KObject] with the specified fields added or updated.
     *
     * @param fields The fields to add or update.
     * @return A new [KObject] with the updated fields.
     */
    fun withAll(fields: Iterable<Field<*>>): KObject

    /**
     * Creates a new [KObject] with the specified key removed.
     *
     * @param key The key to remove.
     * @return A new [KObject] without the specified key.
     */
    fun without(key: Key<*>): KObject

    /**
     * Creates a new [KObject] with the specified keys removed.
     *
     * @param keys The keys to remove.
     * @return A new [KObject] without the specified keys.
     */
    fun withoutAll(keys: Set<Key<*>>): KObject
}

/**
 * Represents a mutable version of [KObject], allowing for modifications.
 */
interface MutableKObject : KObject {

    /**
     * Associates the specified value with the specified key.
     *
     * @param key The key to associate the value with.
     * @param value The value to associate with the key.
     */
    fun <V> put(key: Key<V>, value: V)

    /**
     * Associates all the specified fields with their keys.
     *
     * @param fields The fields to associate.
     */
    fun putAll(fields: Iterable<Field<*>>)

    /**
     * Removes the value associated with the specified key.
     *
     * @param key The key to remove the value for.
     */
    fun remove(key: Key<*>)

    /**
     * Removes the values associated with the specified keys.
     *
     * @param keys The keys to remove values for.
     */
    fun removeAll(keys: Set<Key<*>>)

    /**
     * Computes a value if absent for the specified key and associates it.
     *
     * @param key The key to compute the value for if absent.
     * @param compute The function to compute the value.
     * @return The existing or newly computed value.
     */
    fun <V> computeIfAbsent(key: Key<V>, compute: () -> V?): V?

    override fun with(field: Field<*>): MutableKObject

    override fun withAll(fields: Iterable<Field<*>>): MutableKObject

    override fun without(key: Key<*>): MutableKObject

    override fun withoutAll(keys: Set<Key<*>>): MutableKObject
}
