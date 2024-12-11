package ru.nishiol.kobject

import ru.nishiol.kobject.KObject.Field
import ru.nishiol.kobject.KObject.Key

class MapKObjectStore(private val map: Map<Key<Any?>, Any?>) :
    KObjectStore by MutableMapKObjectStore(map.toMutableMap()) {

    override val keys: Set<Key<Any?>> = map.keys
}

class MutableMapKObjectStore(private val map: MutableMap<Key<Any?>, Any?>) : MutableKObjectStore {
    override val keys: Set<Key<Any?>> get() = map.keys

    override fun get(key: Key<Any?>): Any? = map[key]

    override fun contains(key: Key<Any?>): Boolean = map.containsKey(key)

    override fun put(key: Key<Any?>, value: Any?) {
        map[key] = value
    }

    override fun remove(key: Key<Any?>) {
        map.remove(key)
    }

    override fun computeIfAbsent(key: Key<Any?>, compute: () -> Any?): Any? = map.computeIfAbsent(key) { compute() }

    override fun with(field: Field<Any?>): MutableMapKObjectStore =
        MutableMapKObjectStore(map.toMutableMap().apply { put(field.key, field.value) })

    override fun withAll(fields: Iterable<Field<Any?>>): MutableMapKObjectStore =
        MutableMapKObjectStore(map.toMutableMap().apply { putAll(fields.map { it.toPair() }) })

    override fun without(key: Key<Any?>): MutableKObjectStore =
        MutableMapKObjectStore(map.toMutableMap().apply { remove(key) })

    override fun withoutAll(keys: Set<Key<Any?>>): MutableMapKObjectStore =
        MutableMapKObjectStore(map.toMutableMap().apply { this.keys.removeAll(keys) })
}