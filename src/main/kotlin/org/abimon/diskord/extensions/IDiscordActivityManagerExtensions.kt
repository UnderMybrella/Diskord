package org.abimon.diskord.extensions

import discordgamesdk.DiscordActivity
import discordgamesdk.IDiscordActivityManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.abimon.diskord.*
import kotlin.coroutines.resume

fun IDiscordActivityManager.registerCommand(command: String) {
    memScoped(DISCORD_PATH_LENGTH.toLong()) { ptr ->
        ptr.setString(0, command)
        register_command.apply(this, ptr)
    }
}

fun IDiscordActivityManager.registerSteam(steamId: Int) {
    register_steam.apply(this, steamId)
}

fun IDiscordActivityManager.updateActivity(activity: DiscordActivity, callback: (activityManager: IDiscordActivityManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        update_activity.apply(this, activity, ptr) { callbackData, result -> callback(IDiscordActivityManager(callbackData), result) }
    }
}
suspend fun IDiscordActivityManager.updateActivityAwait(activity: DiscordActivity, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
        withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine<Int> { coroutine -> updateActivity(activity) { _, result -> coroutine.resume(result) } }
        }
fun IDiscordActivityManager.updateActivityBlock(activity: DiscordActivity, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { updateActivityAwait(activity, timeout) }

fun IDiscordActivityManager.clearActivity(callback: (activityManager: IDiscordActivityManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        clear_activity.apply(this, ptr) { callbackData, result -> callback(IDiscordActivityManager(callbackData), result) }
    }
}
suspend fun IDiscordActivityManager.clearActivityAwait(timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
    withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<Int> { coroutine ->
            scopedPointer { ptr ->
                clear_activity.apply(this@clearActivityAwait, ptr) { _, result -> coroutine.resume(result) }
            }
        }
    }
fun IDiscordActivityManager.clearActivityBlock(timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { clearActivityAwait(timeout) }

fun IDiscordActivityManager.sendRequestReply(userId: Long, reply: Int, callback: (activityManager: IDiscordActivityManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        send_request_reply.apply(this, userId, reply, ptr) { callbackPtr, result -> callback(IDiscordActivityManager(callbackPtr), result) }
    }
}
suspend fun IDiscordActivityManager.sendRequestReplyAwait(userId: Long, reply: Int, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
        withTimeoutOrNull(timeout) {
            suspendCancellableCoroutine<Int> { coroutine ->
                scopedPointer { ptr ->
                    send_request_reply.apply(this@sendRequestReplyAwait, userId, reply, ptr) { _, result -> coroutine.resume(result) }
                }
            }
        }
fun IDiscordActivityManager.sendRequestReplyBlock(userId: Long, reply: Int, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { sendRequestReplyAwait(userId, reply, timeout) }

fun IDiscordActivityManager.sendInvite(userId: Long, type: Int, content: String, callback: (activityManager: IDiscordActivityManager, result: Int) -> Unit) {
    memScoped(DISCORD_MESSAGE_LENGTH.toLong()) { contentMem ->
        contentMem.setString(0, content)
        scopedPointer { ptr ->
            send_invite.apply(this, userId, type, contentMem, ptr) { callbackPtr, result ->
                callback(
                    IDiscordActivityManager(callbackPtr),
                    result
                )
            }
        }
    }
}
suspend fun IDiscordActivityManager.sendInviteAwait(userId: Long, type: Int, content: String, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
    withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<Int> { coroutine ->
            memScoped(DISCORD_MESSAGE_LENGTH.toLong()) { contentMem ->
                contentMem.setString(0, content)
                scopedPointer { ptr ->
                    send_invite.apply(
                        this@sendInviteAwait,
                        userId,
                        type,
                        contentMem,
                        ptr
                    ) { _, result -> coroutine.resume(result) }
                }
            }
        }
    }
fun IDiscordActivityManager.sendInviteBlock(userId: Long, type: Int, content: String, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { sendInviteAwait(userId, type, content, timeout) }

fun IDiscordActivityManager.acceptInvite(userId: Long, callback: (activityManager: IDiscordActivityManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        accept_invite.apply(this, userId, ptr) { callbackPtr, result -> callback(IDiscordActivityManager(callbackPtr), result) }
    }
}
suspend fun IDiscordActivityManager.acceptInviteAwait(userId: Long, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
    withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<Int> { coroutine ->
            scopedPointer { ptr ->
                accept_invite.apply(this@acceptInviteAwait, userId, ptr) { _, result -> coroutine.resume(result) }
            }
        }
    }
fun IDiscordActivityManager.acceptInviteBlock(userId: Long, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { acceptInviteAwait(userId, timeout) }
