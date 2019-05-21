package org.abimon.diskord.extensions

import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.LongByReference
import discordgamesdk.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import org.abimon.diskord.*
import kotlin.coroutines.resume

fun IDiscordLobbyManager.lobbyCreateTransaction(): DiscordResult<IDiscordLobbyTransaction> =
    construct { ptr -> get_lobby_create_transaction.apply(this, ptr) }
fun IDiscordLobbyManager.lobbyUpdateTransaction(lobbyId: DiscordLobbyId): DiscordResult<IDiscordLobbyTransaction> =
    construct { ptr -> get_lobby_update_transaction.apply(this, lobbyId, ptr) }
fun IDiscordLobbyManager.memberUpdateTransaction(lobbyId: DiscordLobbyId, userId: DiscordUserId): DiscordResult<IDiscordLobbyMemberTransaction> =
    construct { ptr -> get_member_update_transaction.apply(this, lobbyId, userId, ptr) }

fun IDiscordLobbyManager.createLobby(transaction: IDiscordLobbyTransaction, callback: (lobbyManager: IDiscordLobbyManager, result: Int, lobby: DiscordLobby) -> Unit) {
    scopedPointer { ptr ->
        create_lobby.apply(this, transaction, ptr) { callbackData, result, lobby -> callback(
            lobbyManager(
                callbackData
            ), result, lobby) }
    }
}
fun IDiscordLobbyManager.createLobbyBlock(transaction: IDiscordLobbyTransaction, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordLobby> = runBlocking { createLobbyAwait(transaction, timeout) }
suspend fun IDiscordLobbyManager.createLobbyAwait(transaction: IDiscordLobbyTransaction, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordLobby> =
    awaitDiscordResult(timeout) { coroutine ->
        createLobby(transaction) { _, result, lobby ->
            coroutine.resume(
                DiscordResult(result, lobby)
            )
        }
    }

fun IDiscordLobbyManager.updateLobby(lobbyId: DiscordLobbyId, transaction: IDiscordLobbyTransaction, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        update_lobby.apply(this, lobbyId, transaction, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.updateLobbyBlock(lobbyId: DiscordLobbyId, transaction: IDiscordLobbyTransaction, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { updateLobbyAwait(lobbyId, transaction, timeout) }
suspend fun IDiscordLobbyManager.updateLobbyAwait(lobbyId: DiscordLobbyId, transaction: IDiscordLobbyTransaction, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
    withTimeoutOrNull(timeout) {
        suspendCancellableCoroutine<Int> { coroutine ->
            updateLobby(lobbyId, transaction) { _, result -> coroutine.resume(result) }
        }
    }

fun IDiscordLobbyManager.deleteLobby(lobbyId: DiscordLobbyId, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        delete_lobby.apply(this, lobbyId, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.deleteLobbyBlock(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> = runBlocking { deleteLobbyAwait(lobbyId, timeout) }
suspend fun IDiscordLobbyManager.deleteLobbyAwait(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> =
    awaitDiscordResult(timeout) { coroutine ->
        deleteLobby(lobbyId) { _, result ->
            coroutine.resume(
                DiscordResult(result)
            )
        }
    }

fun IDiscordLobbyManager.connectToLobby(lobbyId: DiscordLobbyId, secret: DiscordLobbySecret, callback: (lobbyManager: IDiscordLobbyManager, result: Int, lobby: DiscordLobby) -> Unit) {
    memScoped(secret) { mem ->
        scopedPointer { ptr ->
            connect_lobby.apply(
                this,
                lobbyId,
                mem,
                ptr
            ) { callbackData, result, lobby ->
                callback(
                    lobbyManager(callbackData),
                    result,
                    lobby
                )
            }
        }
    }
}
fun IDiscordLobbyManager.connectToLobbyBlock(lobbyId: DiscordLobbyId, secret: DiscordLobbySecret, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordLobby>? = runBlocking { connectToLobbyAwait(lobbyId, secret, timeout)}
suspend fun IDiscordLobbyManager.connectToLobbyAwait(lobbyId: DiscordLobbyId, secret: DiscordLobbySecret, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordLobby> =
    awaitDiscordResult(timeout) { coroutine ->
        connectToLobby(
            lobbyId,
            secret
        ) { _, result, lobby -> coroutine.resume(DiscordResult(result, lobby)) }
    }

fun IDiscordLobbyManager.connectToLobby(activitySecret: DiscordLobbySecret, callback: (lobbyManager: IDiscordLobbyManager, result: Int, lobby: DiscordLobby) -> Unit) {
    memScoped(activitySecret) { mem ->
        scopedPointer { ptr ->
            connect_lobby_with_activity_secret.apply(
                this,
                mem,
                ptr
            ) { callbackData, result, lobby ->
                callback(
                    lobbyManager(callbackData),
                    result,
                    lobby
                )
            }
        }
    }
}
fun IDiscordLobbyManager.connectToLobbyBlock(activitySecret: DiscordLobbySecret, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordLobby> = runBlocking { connectToLobbyAwait(activitySecret, timeout)}
suspend fun IDiscordLobbyManager.connectToLobbyAwait(activitySecret: DiscordLobbySecret, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<DiscordLobby> =
    awaitDiscordResult(timeout) { coroutine ->
        connectToLobby(activitySecret) { _, result, lobby ->
            coroutine.resume(
                DiscordResult(result, lobby)
            )
        }
    }

fun IDiscordLobbyManager.disconnectFromLobby(lobbyId: DiscordLobbyId, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        disconnect_lobby.apply(this, lobbyId, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.disconnectFromLobbyBlock(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> = runBlocking { disconnectFromLobbyAwait(lobbyId, timeout) }
suspend fun IDiscordLobbyManager.disconnectFromLobbyAwait(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> =
    awaitDiscordResult(timeout) { coroutine ->
        disconnectFromLobby(lobbyId) { _, result ->
            coroutine.resume(
                DiscordResult(result)
            )
        }
    }

fun IDiscordLobbyManager.getLobby(lobbyId: DiscordLobbyId): DiscordResult<DiscordLobby> {
    val lobby = DiscordLobby()
    val response = get_lobby.apply(this, lobbyId, lobby)
    return DiscordResult(response, lobby).filter { l -> l.type != 0 }
}

fun IDiscordLobbyManager.getLobbyActivitySecret(lobbyId: DiscordLobbyId): DiscordResult<String> =
    memScoped(DISCORD_LOBBY_SECRET_LENGTH.toLong()) { mem ->
        DiscordResult(get_lobby_activity_secret.apply(this, lobbyId, mem), mem.getString(0))
    }

fun IDiscordLobbyManager.getLobbyMetadataKey(lobbyId: DiscordLobbyId, index: Int): DiscordResult<String> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { mem ->
        DiscordResult(get_lobby_metadata_key.apply(this, lobbyId, index, mem), mem.getString(0))
    }

fun IDiscordLobbyManager.getLobbyMetadataValue(lobbyId: DiscordLobbyId, key: String): DiscordResult<String> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { keyMem ->
        keyMem.writeUTF8(key)
        memScoped(DISCORD_METADATA_VALUE_LENGTH.toLong()) { valueMem ->
            DiscordResult(
                get_lobby_metadata_value.apply(this, lobbyId, keyMem, valueMem),
                valueMem.readUTF8()
            )
        }
    }

fun IDiscordLobbyManager.getLobbyMetadataCount(lobbyId: DiscordLobbyId): DiscordResult<Int> {
    val ref = IntByReference()
    return DiscordResult(lobby_metadata_count.apply(this, lobbyId, ref), ref.value)
}

fun IDiscordLobbyManager.getMemberCount(lobbyId: DiscordLobbyId): DiscordResult<Int> {
    val ref = IntByReference()
    return DiscordResult(member_count.apply(this, lobbyId, ref), ref.value)
}

fun IDiscordLobbyManager.getMemberId(lobbyId: DiscordLobbyId, index: Int): DiscordResult<DiscordUserId> {
    val ref = LongByReference()
    return DiscordResult(get_member_user_id.apply(this, lobbyId, index, ref), ref.value)
}

fun IDiscordLobbyManager.getMember(lobbyId: DiscordLobbyId, userId: DiscordUserId): DiscordResult<DiscordUser> =
        DiscordUser().let { user ->
            DiscordResult(
                get_member_user.apply(this, lobbyId, userId, user),
                user
            )
        }

fun IDiscordLobbyManager.getMemberAt(lobbyId: DiscordLobbyId, index: Int): DiscordResult<DiscordUser> =
        getMemberId(lobbyId, index).flatMap { userId -> getMember(lobbyId, userId) }

fun IDiscordLobbyManager.getMembers(lobbyId: DiscordLobbyId): DiscordResult<Sequence<DiscordResult<DiscordUser>>> =
        getMemberCount(lobbyId).map { (0 until it).asSequence().map { i -> getMemberAt(lobbyId, i) } }

fun IDiscordLobbyManager.getMemberMetadataKey(lobbyId: DiscordLobbyId, userId: DiscordUserId, index: Int): DiscordResult<String> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { mem ->
        DiscordResult(
            get_member_metadata_key.apply(this, lobbyId, userId, index, mem),
            mem.getString(0)
        )
    }

fun IDiscordLobbyManager.getMemberMetadataValue(lobbyId: DiscordLobbyId, userId: DiscordUserId, key: String): DiscordResult<String> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { keyMem ->
        keyMem.writeUTF8(key)
        memScoped(DISCORD_METADATA_VALUE_LENGTH.toLong()) { valueMem ->
            DiscordResult(
                get_member_metadata_value.apply(this, lobbyId, userId, keyMem, valueMem),
                valueMem.readUTF8()
            )
        }
    }

fun IDiscordLobbyManager.getMemberMetadataCount(lobbyId: DiscordLobbyId, userId: DiscordUserId): DiscordResult<Int> {
    val ref = IntByReference()
    return DiscordResult(member_metadata_count.apply(this, lobbyId, userId, ref), ref.value)
}

fun IDiscordLobbyManager.updateMember(lobbyId: DiscordLobbyId, userId: DiscordUserId, transaction: IDiscordLobbyMemberTransaction, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        update_member.apply(this, lobbyId, userId, transaction, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.updateMemberBlock(lobbyId: DiscordLobbyId, userId: DiscordUserId, transaction: IDiscordLobbyMemberTransaction, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> = runBlocking { updateMemberAwait(lobbyId, userId, transaction, timeout) }
suspend fun IDiscordLobbyManager.updateMemberAwait(lobbyId: DiscordLobbyId, userId: DiscordUserId, transaction: IDiscordLobbyMemberTransaction, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> =
    awaitDiscordResult(timeout) { coroutine ->
        updateMember(
            lobbyId,
            userId,
            transaction
        ) { _, result -> coroutine.resume(DiscordResult(result)) }
    }

fun IDiscordLobbyManager.sendLobbyMessage(lobbyId: DiscordLobbyId, data: ByteArray, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    memScoped(data) { memory ->
        scopedPointer { ptr ->
            send_lobby_message.apply(
                this@sendLobbyMessage,
                lobbyId,
                memory,
                memory.size().toInt(),
                ptr
            ) { callbackData, result -> callback(lobbyManager(callbackData), result) }
        }
    }
}
fun IDiscordLobbyManager.sendLobbyMessage(lobbyId: DiscordLobbyId, message: String, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) = sendLobbyMessage(lobbyId, message.toByteArray(Charsets.UTF_8), callback)
fun IDiscordLobbyManager.sendLobbyMessageBlock(lobbyId: DiscordLobbyId, data: ByteArray, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { sendLobbyMessageAwait(lobbyId, data, timeout) }
fun IDiscordLobbyManager.sendLobbyMessageBlock(lobbyId: DiscordLobbyId, message: String, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = runBlocking { sendLobbyMessageAwait(lobbyId, message, timeout) }
suspend fun IDiscordLobbyManager.sendLobbyMessageAwait(lobbyId: DiscordLobbyId, message: String, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? = sendLobbyMessageAwait(lobbyId, message.toByteArray(Charsets.UTF_8), timeout)
suspend fun IDiscordLobbyManager.sendLobbyMessageAwait(lobbyId: DiscordLobbyId, data: ByteArray, timeout: Long = Diskord.DEFAULT_TIMEOUT): Int? =
    awaitResultWithTimeoutOrNull(timeout) { coroutine ->
        sendLobbyMessage(
            lobbyId,
            data
        ) { _, result -> coroutine.resume(result) }
    }

fun IDiscordLobbyManager.getSearchQuery(): DiscordResult<IDiscordLobbySearchQuery> =
    construct { ptr -> get_search_query.apply(this, ptr) }

fun IDiscordLobbyManager.search(query: IDiscordLobbySearchQuery, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        search.apply(this, query, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.searchBlock(query: IDiscordLobbySearchQuery, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> = runBlocking { searchAwait(query, timeout) }
fun IDiscordLobbyManager.searchBlock(timeout: Long = Diskord.DEFAULT_TIMEOUT, queryInit: IDiscordLobbySearchQuery.() -> Unit): DiscordResult<Unit> = runBlocking { searchAwait(timeout, queryInit) }
suspend fun IDiscordLobbyManager.searchAwait(timeout: Long = Diskord.DEFAULT_TIMEOUT, queryInit: IDiscordLobbySearchQuery.() -> Unit): DiscordResult<Unit> = getSearchQuery().ifPresent { query -> query.queryInit() }.flatMapSuspend { query -> searchAwait(query, timeout) }
suspend fun IDiscordLobbyManager.searchAwait(query: IDiscordLobbySearchQuery, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> =
    awaitDiscordResult(timeout) { coroutine ->
        search(query) { _, result ->
            coroutine.resume(
                DiscordResult(
                    result
                )
            )
        }
    }

fun IDiscordLobbyManager.searchLobbyCount(): Int {
    val reference = IntByReference()
    lobby_count.apply(this, reference)
    return reference.value
}
fun IDiscordLobbyManager.getLobbyIdAt(index: Int): DiscordResult<DiscordLobbyId> {
    val ref = LongByReference()
    return DiscordResult(get_lobby_id.apply(this, index, ref), ref.value)
}
fun IDiscordLobbyManager.getLobbyAt(index: Int): DiscordResult<DiscordLobby> =
        getLobbyIdAt(index)
            .flatMap(this::getLobby)
fun IDiscordLobbyManager.searchResults(): Sequence<DiscordResult<DiscordLobby>> =
    (0 until searchLobbyCount()).asSequence().map(this::getLobbyAt)

fun IDiscordLobbyManager.connectToVoice(lobbyId: DiscordLobbyId, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        connect_voice.apply(this, lobbyId, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.connectToVoiceBlock(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> = runBlocking { connectToVoiceAwait(lobbyId, timeout)}
suspend fun IDiscordLobbyManager.connectToVoiceAwait(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> =
    awaitDiscordResult(timeout) { coroutine ->
        connectToVoice(lobbyId) { _, result ->
            coroutine.resume(
                DiscordResult(result)
            )
        }
    }

fun IDiscordLobbyManager.disconnectToVoice(lobbyId: DiscordLobbyId, callback: (lobbyManager: IDiscordLobbyManager, result: Int) -> Unit) {
    scopedPointer { ptr ->
        disconnect_voice.apply(this, lobbyId, ptr) { callbackData, result -> callback(
            lobbyManager(
                callbackData
            ), result) }
    }
}
fun IDiscordLobbyManager.disconnectToVoiceBlock(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> = runBlocking { disconnectToVoiceAwait(lobbyId, timeout)}
suspend fun IDiscordLobbyManager.disconnectToVoiceAwait(lobbyId: DiscordLobbyId, timeout: Long = Diskord.DEFAULT_TIMEOUT): DiscordResult<Unit> =
    awaitDiscordResult(timeout) { coroutine ->
        disconnectToVoice(lobbyId) { _, result ->
            coroutine.resume(
                DiscordResult(result)
            )
        }
    }

fun IDiscordLobbyManager.connectNetworkLayer(lobbyId: Long): DiscordResult<Unit> =
    DiscordResult(connect_network.apply(this, lobbyId))

fun IDiscordLobbyManager.disconnectNetworkLayer(lobbyId: Long): DiscordResult<Unit> =
    DiscordResult(disconnect_network.apply(this, lobbyId))

fun IDiscordLobbyManager.flushNetwork(): DiscordResult<Unit> =
    DiscordResult(flush_network.apply(this))

fun IDiscordLobbyManager.openNetworkChannel(lobbyId: DiscordLobbyId, channelId: Int, reliable: Boolean): DiscordResult<Unit> =
    DiscordResult(
        open_network_channel.apply(
            this,
            lobbyId,
            channelId.toByte(),
            if (reliable) 1 else 0
        )
    )

fun IDiscordLobbyManager.sendNetworkMessage(lobbyId: DiscordLobbyId, userId: DiscordUserId, channelId: Int, message: String) = sendNetworkMessage(lobbyId, userId, channelId, message.toByteArray(Charsets.UTF_8))
fun IDiscordLobbyManager.sendNetworkMessage(lobbyId: DiscordLobbyId, userId: DiscordUserId, channelId: Int, data: ByteArray): DiscordResult<Unit> =
    memScoped(data) { mem ->
        DiscordResult(
            send_network_message.apply(
                this,
                lobbyId,
                userId,
                channelId.toByte(),
                mem,
                mem.size().toInt()
            )
        )
    }

fun lobbyManager(ptr: Pointer): IDiscordLobbyManager {
    val manager = IDiscordLobbyManager(ptr)
    manager.read()
    return manager
}