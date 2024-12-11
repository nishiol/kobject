# KObject

`KObject` implements a dynamic object where fields are defined at runtime. It is similar to a JavaScript object or a `Map<String, Any?>`, but it allows retrieving and modifying values without losing type information.

---

## Advantages

- Lightweight
- No external dependencies
- No reflection usage
- Type-safe access and modification of values (when used correctly)
- Intuitive operators (`+`, `-`, etc.)

---

## Installation

Add the following dependency to your project:

### Gradle

```kotlin
implementation("ru.nishiol.kobject:kobject:1.0.0")
```

### Maven

```xml

<dependency>
    <groupId>ru.nishiol.kobject</groupId>
    <artifactId>kobject</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## Getting Started

### Type-Safe Access

`KObject` ensures type-safe methods to read and modify values:

```kotlin
import ru.nishiol.kobject.*

fun main() {
    // Define keys
    val stringKey = object : KObject.Key<String> {}
    val intKey = object : KObject.Key<Int> {}

    // Create fields
    val stringField = KObject.Field(stringKey, "Hello, Kotlin!")
    val intField = KObject.Field(intKey, 100)

    // Create a KObject
    val kObject = kObjectOf(stringField, intField)

    // Type-safe value access
    val message: String? = kObject[stringKey]
    val number: Int? = kObject[intKey]

    println(message) // Output: Hello, Kotlin!
    println(number)  // Output: 100
}
```

### A Note on Type Safety

Although `KObject` is designed for type-safe access, it relies on the developer's judgment. Type safety can be bypassed
using unsafe casts, which may result in runtime exceptions. For example:

```kotlin
val key1 = object : KObject.Key<String> {}
val key2 = key1 as KObject.Key<Int> // Unsafe cast

val field = KObject.Field(key1, "hello")
val kObject = kObjectOf(field)

println(3 + kObject[key2]) // Throws ClassCastException
```

The library does not try to outsmart the developer and assumes responsible usage. Ensure correct key-value type
associations to maintain safety.

---

### Modifying Values

```kotlin
// Create a mutable KObject
val mutableKObject = kObject.toMutableKObject()

// Type-safe value modification
mutableKObject[stringKey] = "Updated Message"
mutableKObject[intKey] = 200

println(mutableKObject[stringKey]) // Output: Updated Message
println(mutableKObject[intKey])    // Output: 200
```

---

### Concurrent KObject

`KObject` supports concurrent mutable stores for thread-safe applications:

```kotlin
val key = object : KObject.Key<String> {}
val field = KObject.Field(key, "Concurrent Value")

val concurrentStore = mutableKObjectOf(field, concurrent = true)
concurrentStore[key] = "Updated Concurrent Value"
println(concurrentStore[key]) // Output: Updated Concurrent Value
```

---

### Operators

`KObject` provides intuitive operator functions for adding and removing fields:

```kotlin
val key1 = object : KObject.Key<Int> {}
val key2 = object : KObject.Key<String> {}
val field1 = KObject.Field(key1, 42)
val field2 = KObject.Field(key2, "Example")

val kObject = kObjectOf(field1)

// Add fields
val updatedKObject = kObject + field2

// Remove fields
val reducedKObject = updatedKObject - key2

println(updatedKObject[key2]) // Output: Example
println(reducedKObject[key2]) // Output: null
```

---

### More Examples

Additional examples can be found in the tests.

## Contributing

Contributions are welcome! Feel free to submit issues, feature requests, or pull requests.

---

## License

This project is licensed under the [MIT License](LICENSE).
