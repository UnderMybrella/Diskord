package org.abimon.diskord

import discordgamesdk.DiscordGameSDKLibrary

object Diskord : DiscordGameSDKLibrary by DiscordGameSDKLibrary.INSTANCE {
    var DEFAULT_TIMEOUT = 5000L
}