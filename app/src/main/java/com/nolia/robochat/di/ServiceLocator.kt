package com.nolia.robochat.di

interface Injectable

@Suppress("unused")
inline fun <reified T> Injectable.get() = ServiceLocator.get(T::class.java)

inline fun <reified T> Injectable.inject() = lazy { get<T>() }

object ServiceLocator {

    private val mapping = mutableMapOf<Class<*>, Any>()

    fun <T> set(clazz: Class<T>, instance: T) {
        mapping[clazz] = instance as Any
    }

    inline fun <reified T> set(instance: T) = set(T::class.java, instance)

    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: Class<T>): T {
        return mapping[clazz] as? T ?: throw IllegalArgumentException("Mapping not found!")
    }

}
