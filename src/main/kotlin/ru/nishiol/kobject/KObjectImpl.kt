package ru.nishiol.kobject

import ru.nishiol.kobject.KObject.*

internal class KObjectImpl(private val store: KObjectStore) : KObject {
    override val keys: Set<Key<*>> = store.keys

    @Suppress("UNCHECKED_CAST")
    override fun <V> get(key: Key<V>): V? = store.get(key) as V?

    override fun getAll(keys: Set<Key<*>>): Fields = Fields(store.getAll(keys))

    override fun contains(key: Key<*>): Boolean = store.contains(key)

    override fun existingKeys(keys: Set<Key<*>>): Set<Key<*>> = store.existingKeys(keys)

    override fun with(field: Field<*>): KObject = KObjectImpl(store.with(field))

    override fun withAll(fields: Iterable<Field<*>>): KObject = KObjectImpl(store.withAll(fields))

    override fun without(key: Key<*>): KObject = KObjectImpl(store.without(key))

    override fun withoutAll(keys: Set<Key<*>>): KObject = KObjectImpl(store.withoutAll(keys))
}

internal class MutableKObjectImpl(private val store: MutableKObjectStore) : MutableKObject {
    override val keys: Set<Key<*>> get() = store.keys

    @Suppress("UNCHECKED_CAST")
    override fun <V> get(key: Key<V>): V? = store.get(key) as V?

    override fun getAll(keys: Set<Key<*>>): Fields = Fields(store.getAll(keys))

    override fun contains(key: Key<*>): Boolean = store.contains(key)

    override fun existingKeys(keys: Set<Key<*>>): Set<Key<*>> = store.existingKeys(keys)

    override fun <V> put(key: Key<V>, value: V) {
        store.put(key, value)
    }

    override fun putAll(fields: Iterable<Field<*>>) {
        store.putAll(fields)
    }

    override fun remove(key: Key<*>) {
        store.remove(key)
    }

    override fun removeAll(keys: Set<Key<*>>) {
        store.removeAll(keys)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <V> computeIfAbsent(key: Key<V>, compute: () -> V?): V? = store.computeIfAbsent(key, compute) as V?

    override fun with(field: Field<*>): MutableKObject = MutableKObjectImpl(store.with(field))

    override fun withAll(fields: Iterable<Field<*>>): MutableKObject = MutableKObjectImpl(store.withAll(fields))

    override fun without(key: Key<*>): MutableKObject = MutableKObjectImpl(store.without(key))

    override fun withoutAll(keys: Set<Key<*>>): MutableKObject = MutableKObjectImpl(store.withoutAll(keys))
}
