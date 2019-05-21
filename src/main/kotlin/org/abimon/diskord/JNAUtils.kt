package org.abimon.diskord

import com.sun.jna.Memory
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.PointerByReference
import java.io.Closeable

class ScopedMemory(size: Long): Memory(size), Closeable {
    override fun close() =
            dispose()
}

fun memoryOf(value: Int): Memory {
    val mem = Memory(4)
    mem.setInt(0, value)
    return mem
}

fun memoryOf(value: String): Memory {
    val data = value.toByteArray(Charsets.UTF_8)
    val mem = Memory(data.size + 1L)
    mem.write(0, data, 0, data.size)
    return mem
}

inline fun <T> memScoped(size: Long, op: (Memory) -> T): T = ScopedMemory(size).use(op)
inline fun <T> memScoped(data: ByteArray, op: (Memory) -> T): T {
    val mem = ScopedMemory(data.size.toLong())
    mem.write(0, data, 0, data.size)
    return mem.use(op)
}
inline fun <T> memScoped(value: String, op: (Memory) -> T): T {
    val data = value.toByteArray(Charsets.UTF_8)
    val mem = ScopedMemory(data.size + 1L)
    mem.write(0, data, 0, data.size)
    return mem.use(op)
}

fun <T: Structure, R> T.scopedPointer(op: (Pointer) -> R): R {
    try {
        write()
        return op(pointer)
    } finally {
        read()
    }
}

fun Memory.writeUTF8(string: String, offset: Long = 0) = setString(offset, string, "UTF-8")
fun Pointer.readUTF8(offset: Long = 0): String = getString(offset, "UTF-8")

inline fun <reified T: Structure> construct(op: (PointerByReference) -> Int): DiscordResult<T> {
    val ptr = PointerByReference()
    val result = op(ptr)
    return DiscordResult(result, Structure.newInstance(T::class.java, ptr.value).also(Structure::read))
}