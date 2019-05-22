package org.abimon.diskord

import com.sun.jna.Structure
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.*

fun <T> byteArray(byteArrayProperty: KProperty0<ByteArray>): ReadOnlyProperty<T, String> =
    object : ReadOnlyProperty<T, String> {
        override fun getValue(thisRef: T, property: KProperty<*>): String =
            String(byteArrayProperty.get(), Charsets.UTF_8).substringBefore('\u0000')
    }

fun <T> byteArray(byteArrayProperty: KProperty1<T, ByteArray>): ReadOnlyProperty<T, String> =
    object : ReadOnlyProperty<T, String> {
        override fun getValue(thisRef: T, property: KProperty<*>): String =
            String(byteArrayProperty.get(thisRef), Charsets.UTF_8).substringBefore('\u0000')
    }

fun <T> mutableByteArray(byteArrayProperty: KMutableProperty0<ByteArray>): ReadWriteProperty<T, String> =
    object :
        ReadWriteProperty<T, String> {
        override fun getValue(thisRef: T, property: KProperty<*>): String =
            String(byteArrayProperty.get(), Charsets.UTF_8).substringBefore('\u0000')

        override fun setValue(thisRef: T, property: KProperty<*>, value: String) {
            val array = byteArrayProperty.get()
            Arrays.fill(array, 0)
            value.toByteArray(Charsets.UTF_8).copyInto(array)
            byteArrayProperty.set(array)
        }
    }

fun <T> mutableByteArray(byteArrayProperty: KMutableProperty1<T, ByteArray>): ReadWriteProperty<T, String> =
    object :
        ReadWriteProperty<T, String> {
        override fun getValue(thisRef: T, property: KProperty<*>): String =
            String(byteArrayProperty.get(thisRef), Charsets.UTF_8).substringBefore('\u0000')

        override fun setValue(thisRef: T, property: KProperty<*>, value: String) {
            val array = byteArrayProperty.get(thisRef)
            Arrays.fill(array, 0)
            value.toByteArray(Charsets.UTF_8).copyInto(array)
            byteArrayProperty.set(thisRef, array)
        }
    }


inline fun <reified S: Structure, T, O> structProperty(struct: KMutableProperty0<S?>, readProperty: KMutableProperty1<S, T>): ReadWriteProperty<O, T?> =
        object: ReadWriteProperty<O, T?> {
            val klass = S::class.java

            override fun getValue(thisRef: O, property: KProperty<*>): T? = struct.get()?.let(readProperty::get)

            override fun setValue(thisRef: O, property: KProperty<*>, value: T?) {
                if (struct.get() == null)
                    struct.set(Structure.newInstance(klass))
                struct.get()?.let { s -> readProperty.set(s, value ?: return) }
            }
        }

operator fun <R> KProperty0<R>.getValue(t: Any, prop: KProperty<*>): R = this.get()
operator fun <R> KMutableProperty0<R>.setValue(t: Any, prop: KProperty<*>, value: R) { this.set(value) }
operator fun <T, R> KProperty1<T, R>.getValue(t: T, prop: KProperty<*>): R = this.get(t)
operator fun <T, R> KMutableProperty1<T, R>.setValue(t: T, prop: KProperty<*>, value: R) { this.set(t, value) }