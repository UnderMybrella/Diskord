package org.abimon.diskord.extensions

import com.sun.jna.ptr.IntByReference
import discordgamesdk.DiscordUser
import discordgamesdk.IDiscordUserManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.abimon.diskord.DiscordResult
import org.abimon.diskord.Diskord
import org.abimon.diskord.scopedPointer
import kotlin.coroutines.resume

val IDiscordUserManager.currentUser: DiscordUser
    get() = DiscordUser.ByReference().also { user -> get_current_user.apply(this, user) }

val IDiscordUserManager.currentUserPremiumType: Int
    get() = IntByReference().also { int -> get_current_user_premium_type.apply(this, int) }.value

fun IDiscordUserManager.getUser(userId: Long, callback: (userManager: IDiscordUserManager, result: Int, user: DiscordUser) -> Unit) {
    scopedPointer { ptr ->
        get_user.apply(this, userId, ptr) { callbackData, result, user -> callback(IDiscordUserManager(callbackData), result, user) }
    }
}
suspend fun IDiscordUserManager.getUserAwait(userId: Long, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordUser>? =
        withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine<DiscordResult<DiscordUser>> { coroutine ->
                scopedPointer { ptr ->
                    get_user.apply(this@getUserAwait, userId, ptr) { _, result, user -> coroutine.resume(
                        DiscordResult(
                            result,
                            user
                        )
                    ) }
                }
            }
        }
fun IDiscordUserManager.getUserBlock(userId: Long, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordUser>? = runBlocking { getUserAwait(userId, timeout) }

fun IDiscordUserManager.currentUserHasFlag(flag: Int): Boolean {
    return IntByReference().also { ref -> current_user_has_flag.apply(this, flag, ref.pointer) }.value >= 1
}