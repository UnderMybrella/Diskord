package org.abimon.diskord.builders

import discordgamesdk.*
import org.abimon.diskord.extensions.*
import org.abimon.diskord.getValue
import org.abimon.diskord.mutableByteArray
import org.abimon.diskord.setValue
import org.abimon.diskord.structProperty

class DiscordActivityBuilder {
    private val activity = DiscordActivity()

    var name: String by mutableByteArray(activity::name)
    var state: String by mutableByteArray(activity::state)
    var details: String by mutableByteArray(activity::details)
    var instance: Byte by activity::instance

    var startTimestamp: Long? by structProperty(activity::timestamps, DiscordActivityTimestamps::start)
    var endTimestamp: Long? by structProperty(activity::timestamps, DiscordActivityTimestamps::end)

    var largeImageKey: String? by structProperty(activity::assets, DiscordActivityAssets::largeImageAsString)
    var largeImageText: String? by structProperty(activity::assets, DiscordActivityAssets::largeTextAsString)
    var smallImageKey: String? by structProperty(activity::assets, DiscordActivityAssets::largeImageAsString)
    var smallImageText: String? by structProperty(activity::assets, DiscordActivityAssets::smallTextAsString)

    var partyId: String? by structProperty(activity::party, DiscordActivityParty::idAsString)
    var partySize: DiscordPartySize? by structProperty(activity::party, DiscordActivityParty::partySize)
    
    var partySizeFilled: Int? by structProperty(this::partySize, DiscordPartySize::current_size)
    var partySizeCapacity: Int? by structProperty(this::partySize, DiscordPartySize::max_size)
    
    var matchSecret: String? by structProperty(activity::secrets, DiscordActivitySecrets::matchAsString)
    var joinSecret: String? by structProperty(activity::secrets, DiscordActivitySecrets::joinAsString)
    var spectateSecret: String? by structProperty(activity::secrets, DiscordActivitySecrets::spectateAsString)

    fun build(): DiscordActivity = activity
}

fun buildActivity(init: DiscordActivityBuilder.() -> Unit): DiscordActivity {
    val builder = DiscordActivityBuilder()
    builder.init()
    return builder.build()
}

