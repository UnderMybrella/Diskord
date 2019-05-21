package org.abimon.diskord.extensions

import com.goterl.lazycode.lazysodium.LazySodiumJava
import com.goterl.lazycode.lazysodium.SodiumJava
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.org.apache.xml.internal.security.utils.Base64
import discordgamesdk.DiscordOAuth2Token
import discordgamesdk.IDiscordApplicationManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.abimon.diskord.*
import java.io.File
import javax.xml.bind.DatatypeConverter
import kotlin.coroutines.resume

const val SIGNED_TICKET_VERSION = "2"

val IDiscordApplicationManager.currentLocale: DiscordLocale
    get() =
        memScoped(DISCORD_LOCALE_LENGTH.toLong()) { memory ->
            get_current_locale.apply(this, memory)
            memory.getString(0)
        }

val IDiscordApplicationManager.currentBranch: DiscordBranch
    get() =
        memScoped(DISCORD_BRANCH_LENGTH.toLong()) { memory ->
            get_current_branch.apply(this, memory)
            memory.getString(0)
        }


fun IDiscordApplicationManager.validateOrExit(callback: (applicationManager: IDiscordApplicationManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        validate_or_exit.apply(this, ptr) { callbackPtr, result -> callback(IDiscordApplicationManager(callbackPtr), result) }
    }
}
suspend fun IDiscordApplicationManager.validateOrExitAwait(timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
    withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<Int> { coroutine ->
            scopedPointer { ptr ->
                validate_or_exit.apply(this@validateOrExitAwait, ptr) { _, result -> coroutine.resume(result) }
            }
        }
    }
fun IDiscordApplicationManager.validateOrExitBlock(timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { validateOrExitAwait(timeout) }

fun IDiscordApplicationManager.getOAuth2Token(callback: (applicationManager: IDiscordApplicationManager, result: Int, token: DiscordOAuth2Token) -> Unit) {
    scopedPointer { ptr ->
        get_oauth2_token.apply(this, ptr) { callbackPtr, result, token -> callback(IDiscordApplicationManager(callbackPtr), result, token) }
    }
}
suspend fun IDiscordApplicationManager.getOAuth2TokenAwait(timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordOAuth2Token>? =
    withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<DiscordResult<DiscordOAuth2Token>> { coroutine ->
            scopedPointer { ptr ->
                get_oauth2_token.apply(this@getOAuth2TokenAwait, ptr) { _, result, token ->
                    coroutine.resume(DiscordResult(result, token))
                }
            }
        }
    }
fun IDiscordApplicationManager.getOAuth2TokenBlock(timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordOAuth2Token>? = runBlocking { getOAuth2TokenAwait(timeout) }

fun IDiscordApplicationManager.getTicket(publicKey: String, callback: (applicationManager: IDiscordApplicationManager, result: Int, json: String) -> Unit) = getTicket(DatatypeConverter.parseHexBinary(publicKey), callback)
fun IDiscordApplicationManager.getTicket(publicKey: ByteArray, callback: (applicationManager: IDiscordApplicationManager, result: Int, json: String) -> Unit) {
    if (lazySodium == null)
        return

    scopedPointer { ptr ->
        get_ticket.apply(this, ptr) { callbackPtr, result, dataPtr -> checkTicket(
            publicKey,
            dataPtr ?: return@apply
        )?.let { json -> callback(IDiscordApplicationManager(callbackPtr), result, json) } }
    }
}
suspend fun IDiscordApplicationManager.getTicketAwait(publicKey: String, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<String>? = getTicketAwait(DatatypeConverter.parseHexBinary(publicKey), timeout)
suspend fun IDiscordApplicationManager.getTicketAwait(publicKey: ByteArray, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<String>? {
    if (lazySodium == null)
        return null

    return withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<DiscordResult<String>?> { coroutine ->
            scopedPointer { ptr ->
                get_ticket.apply(this@getTicketAwait, ptr) { _, result, dataPtr ->
                    coroutine.resume(dataPtr?.let {
                        checkTicket(
                            publicKey,
                            it
                        )
                    }?.let { str -> DiscordResult(result, str) })
                }
            }
        }
    }
}
fun IDiscordApplicationManager.getTicketBlock(publicKey: String, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<String>? = runBlocking { getTicketAwait(publicKey, timeout) }
fun IDiscordApplicationManager.getTicketBlock(publicKey: ByteArray, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<String>? = runBlocking { getTicketAwait(publicKey, timeout) }

fun IDiscordApplicationManager.getTicketUnsafe(callback: (applicationManager: IDiscordApplicationManager, result: Int, json: String) -> Unit) {
    scopedPointer { ptr ->
        get_ticket.apply(this, ptr) { callbackPtr, result, dataPtr -> callback(IDiscordApplicationManager(callbackPtr), result, (dataPtr ?: return@apply).getString(0).split('.')[2]) }
    }
}
suspend fun IDiscordApplicationManager.getTicketUnsafeAwait(timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<String>? {
    return withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<DiscordResult<String>?> { coroutine ->
            scopedPointer { ptr ->
                get_ticket.apply(this@getTicketUnsafeAwait, ptr) { _, result, dataPtr ->
                    coroutine.resume(dataPtr?.getString(0)?.let { str -> DiscordResult(result, str) })
                }
            }
        }
    }
}
fun IDiscordApplicationManager.getTicketUnsafeBlock(timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<String>? = runBlocking { getTicketUnsafeAwait(timeout) }

val lazySodium = try { runCatching { loadWindowsDLLs() }; LazySodiumJava(SodiumJava()) } catch (link: UnsatisfiedLinkError) { null }

fun checkTicket(publicKey: ByteArray, ticket: Pointer): String? {
    if (lazySodium == null)
         return null

    val parts = ticket.getString(0).split('.')

    if (parts[0] == SIGNED_TICKET_VERSION) {
        val msgData = parts[2].toByteArray(Charsets.UTF_8)
        if (lazySodium.cryptoSignVerifyDetached(DatatypeConverter.parseHexBinary(parts[1]), msgData, msgData.size, publicKey)) {
            return String(Base64.decode(parts[1]), Charsets.UTF_8)
        }
    }

    return null
}

interface WindowsDlls: Library

const val LIB_WINP_NAME = "libwinpthread-1"
const val LIB_GCC_NAME = "libgcc_s_seh-1"

fun loadWindowsDLLs() {
    var libwinp = Native.extractFromResourcePath(LIB_WINP_NAME)
    var libgcc = Native.extractFromResourcePath(LIB_GCC_NAME)

    if (!libwinp.name.startsWith(LIB_WINP_NAME)) {
        libwinp = File(libwinp.parentFile, "$LIB_WINP_NAME.${libwinp.extension}").also { new -> libwinp.renameTo(new) }
    }

    if (!libgcc.name.startsWith(LIB_GCC_NAME)) {
        libgcc = File(libgcc.parentFile, "$LIB_GCC_NAME.${libgcc.extension}").also { new -> libgcc.renameTo(new) }
    }

    NativeLibrary.getInstance(libwinp.absolutePath)
    NativeLibrary.getInstance(libgcc.absolutePath)
}