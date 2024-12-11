package ru.nishiol.kobject

import ru.nishiol.kobject.KObject.Field
import ru.nishiol.kobject.KObject.Key
import java.util.concurrent.ConcurrentHashMap

fun <V> Field<V>.toPair(): Pair<Key<V>, V> = key to value

fun <V> Pair<Key<V>, V>.toKObjectField(): Field<V> = Field(first, second)

fun kObjectOf(field: Field<Any>, store: KObjectStore = MapKObjectStore(emptyMap())): KObject =
    KObjectImpl(store.with(field))

fun kObjectOf(vararg fields: Field<Any>, store: KObjectStore = MapKObjectStore(emptyMap())): KObject =
    KObjectImpl(store.withAll(fields.asIterable()))

fun mutableKObjectOf(
    field: Field<*>,
    concurrent: Boolean = false,
    store: MutableKObjectStore = MutableMapKObjectStore(if (concurrent) ConcurrentHashMap() else mutableMapOf()),
): MutableKObject = MutableKObjectImpl(store).apply { put(field.key, field.value) }

fun mutableKObjectOf(
    vararg fields: Field<Any>,
    concurrent: Boolean = false,
    store: MutableKObjectStore = MutableMapKObjectStore(if (concurrent) ConcurrentHashMap() else mutableMapOf()),
): MutableKObject = MutableKObjectImpl(store).apply { putAll(fields.asIterable()) }

val KObject.fields: Collection<Field<*>>
    get() = keys.mapTo(mutableListOf()) { Field(it, get(it)) }

fun KObject.toMutableKObject(context: MutableKObject = mutableKObjectOf()): MutableKObject = context.withAll(fields)

fun KObject.toKObject(kObject: KObject = kObjectOf()): KObject = kObject.withAll(fields)

operator fun <V> KObject.get(key: Key<V>): V? = get(key)

fun <V> KObject.getValue(key: Key<V>): V {
    val value = get(key)
    if (value == null && key !in this) {
        throw NoSuchElementException("Key $key is missing in the map.")
    }
    @Suppress("UNCHECKED_CAST")
    return value as V
}

operator fun KObject.contains(key: Key<*>): Boolean = contains(key)

operator fun <V> KObject.plus(pair: Pair<Key<V>, V>): KObject = plus(pair.toKObjectField())

operator fun KObject.plus(field: Field<*>): KObject = with(field)

operator fun KObject.plus(fields: Iterable<Field<*>>): KObject = withAll(fields)

operator fun KObject.plus(other: KObject): KObject = withAll(other.fields)

operator fun KObject.minus(key: Key<*>): KObject = without(key)

operator fun KObject.minus(keys: Set<Key<*>>): KObject = withoutAll(keys)

operator fun KObject.minus(other: KObject): KObject = withoutAll(other.keys)

operator fun <V> MutableKObject.set(key: Key<V>, value: V) {
    put(key, value)
}

operator fun MutableKObject.plusAssign(field: Field<*>) {
    put(field.key, field.value)
}

operator fun MutableKObject.plusAssign(fields: Iterable<Field<*>>) {
    putAll(fields)
}

operator fun MutableKObject.plusAssign(other: KObject) {
    putAll(other.fields)
}

operator fun MutableKObject.minusAssign(key: Key<*>) {
    remove(key)
}

operator fun MutableKObject.minusAssign(keys: Set<Key<*>>) {
    removeAll(keys)
}

operator fun MutableKObject.minusAssign(kObject: KObject) {
    removeAll(kObject.keys)
}