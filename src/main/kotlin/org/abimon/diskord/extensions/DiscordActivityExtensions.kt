package org.abimon.diskord.extensions

import discordgamesdk.*
import org.abimon.diskord.getValue
import org.abimon.diskord.mutableByteArray
import org.abimon.diskord.setValue

var DiscordActivity.stateAsString: String by mutableByteArray(DiscordActivity::state)
var DiscordActivity.detailsAsString: String by mutableByteArray(DiscordActivity::details)
var DiscordActivity.nameAsString: String by mutableByteArray(DiscordActivity::name)

var DiscordActivityParty.idAsString: String by mutableByteArray(DiscordActivityParty::id)
var DiscordActivityParty.partySize: DiscordPartySize
    get() {
        if (size == null)
            size = DiscordPartySize()
        return size
    }
    set(value) {
        size = value
    }

var DiscordActivitySecrets.joinAsString: String by mutableByteArray(DiscordActivitySecrets::join)
var DiscordActivitySecrets.matchAsString: String by mutableByteArray(DiscordActivitySecrets::match)
var DiscordActivitySecrets.spectateAsString: String by mutableByteArray(DiscordActivitySecrets::spectate)

var DiscordActivityAssets.largeImageAsString: String by mutableByteArray(DiscordActivityAssets::large_image)
var DiscordActivityAssets.largeTextAsString: String by mutableByteArray(DiscordActivityAssets::large_text)
var DiscordActivityAssets.smallImageAsString: String by mutableByteArray(DiscordActivityAssets::small_image)
var DiscordActivityAssets.smallTextAsString: String by mutableByteArray(DiscordActivityAssets::small_text)

var DiscordPartySize.currentSize: Int by DiscordPartySize::current_size
var DiscordPartySize.maxSize: Int by DiscordPartySize::max_size
var DiscordPartySize.capacity: Int by DiscordPartySize::max_size

operator fun DiscordPartySize.component1(): Int = current_size
operator fun DiscordPartySize.component2(): Int = max_size