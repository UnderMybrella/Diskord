package org.abimon.diskord.extensions

import discordgamesdk.DiscordUser
import org.abimon.diskord.byteArray

val DiscordUser.usernameAsString: String by byteArray(DiscordUser::username)
val DiscordUser.discriminatorAsString: String by byteArray(DiscordUser::discriminator)

val DiscordUser.displayName: String
    get() = "$usernameAsString#$discriminatorAsString"