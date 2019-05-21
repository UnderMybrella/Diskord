package org.abimon.diskord.extensions

import discordgamesdk.IDiscordLobbyMemberTransaction
import org.abimon.diskord.*

operator fun IDiscordLobbyMemberTransaction.set(key: String, value: String): DiscordResult<Unit> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { memKey ->
        memKey.writeUTF8(key)
        memScoped(DISCORD_METADATA_VALUE_LENGTH.toLong()) { memValue ->
            memValue.writeUTF8(value)

            DiscordResult(set_metadata.apply(this, memKey, memValue))
        }
    }

fun IDiscordLobbyMemberTransaction.delete(key: String): DiscordResult<Unit> =
    memScoped(DISCORD_METADATA_KEY_LENGTH.toLong()) { memKey ->
        memKey.writeUTF8(key)
        DiscordResult(delete_metadata.apply(this, memKey))
    }

operator fun IDiscordLobbyMemberTransaction.minusAssign(key: String) { delete(key) }