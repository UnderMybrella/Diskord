package org.abimon.diskord.extensions

import discordgamesdk.IDiscordLobbyTransaction
import org.abimon.diskord.*

operator fun IDiscordLobbyTransaction.set(key: String, value: String): DiscordResult<Unit> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { memKey ->
        memKey.writeUTF8(key)
        memScoped(DISCORD_METADATA_VALUE_LENGTH.toLong()) { memValue ->
            memValue.writeUTF8(value)

            DiscordResult(set_metadata.apply(this, memKey, memValue))
        }
    }

fun IDiscordLobbyTransaction.delete(key: String): DiscordResult<Unit> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { memKey ->
        memKey.writeUTF8(key)
        DiscordResult(delete_metadata.apply(this, memKey))
    }

operator fun IDiscordLobbyTransaction.minusAssign(key: String) { delete(key) }

fun IDiscordLobbyTransaction.setType(type: Int): DiscordResult<Unit> =
        DiscordResult(set_type.apply(this, type))
fun IDiscordLobbyTransaction.setOwner(ownerId: DiscordUserId): DiscordResult<Unit> =
        DiscordResult(set_owner.apply(this, ownerId))
fun IDiscordLobbyTransaction.setCapacity(capacity: Int): DiscordResult<Unit> =
        DiscordResult(set_capacity.apply(this, capacity))
fun IDiscordLobbyTransaction.setLocked(locked: Boolean): DiscordResult<Unit> =
        DiscordResult(set_locked.apply(this, if (locked) 1 else 0))