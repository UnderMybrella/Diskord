@file:Suppress("EXPERIMENTAL_API_USAGE")

package org.abimon.diskord

import discordgamesdk.*

typealias DiscordClientId = Long
typealias DiscordVersion = Int
typealias DiscordSnowflake = Long
typealias DiscordTimestamp = Long
typealias DiscordUserId = DiscordSnowflake
typealias DiscordLobbyId = DiscordSnowflake
typealias DiscordLocale = String
typealias DiscordBranch = String
typealias DiscordLobbySecret = String
typealias DiscordMetadataKey = String
typealias DiscordMetadataValue = String
typealias DiscordNetworkPeerId = ULong
typealias DiscordNetworkChannelId = UByte
typealias DiscordPath = String
typealias DiscordDateTime = String

val DISCORD_LOCALE_LENGTH = 128
val DISCORD_BRANCH_LENGTH = 4096
val DISCORD_PATH_LENGTH = 4096
val DISCORD_DATETIME_LENGTH = 64
val DISCORD_MESSAGE_LENGTH = 2048
val DISCORD_LOBBY_SECRET_LENGTH = 128
val DISCORD_METADATA_VALUE_LENGTH = 4096
val DISCORD_METADATA_KEY_LENGTH = 256
val DISCORD_USERNAME_LENGTH = 256
val DISCORD_DISCRIMINATOR_LENGTH = 8
val DISCORD_AVATAR_LENGTH = 128
val DISCORD_ACCESS_TOKEN_LENGTH = 128
val DISCORD_SCOPES_LENGTH = 1024
val DISCORD_LARGE_IMAGE_LENGTH = 128
val DISCORD_LARGE_IMAGE_TEXT_LENGTH = 128
val DISCORD_SMALL_IMAGE_LENGTH = 128
val DISCORD_SMALL_IMAGE_TEXT_LENGTH = 128
val DISCORD_PARTY_ID_LENGTH = 128
val DISCORD_MATCH_SECRET_LENGTH = 128
val DISCORD_JOIN_SECRET_LENGTH = 128
val DISCORD_SPECTATE_SECRET_LENGTH = 128
val DISCORD_ACTIVITY_NAME_LENGTH = 128
val DISCORD_ACTIVITY_STATE_LENGTH = 128
val DISCORD_ACTIVITY_DETAILS_LENGTH = 128
val DISCORD_FILENAME_LENGTH = 260
val DISCORD_CURRENCY_LENGTH = 16
val DISCORD_SKU_NAME = 256
val DISCORD_INPUT_SHORTCUT_LENGTH = 256

/** Events */

typealias CurrentUserUpdateHandler=(core: IDiscordCore) -> Unit

typealias ActivityJoinHandler=(core: IDiscordCore, secret: String) -> Unit
typealias ActivitySpectateHandler=(core: IDiscordCore, secret: String) -> Unit
typealias ActivityJoinRequestHandler=(core: IDiscordCore, user: DiscordUser) -> Unit
typealias ActivityInviteHandler=(core: IDiscordCore, type: Int, user: DiscordUser, activity: DiscordActivity) -> Unit

typealias RelationshipRefreshHandler=(core: IDiscordCore) -> Unit
typealias RelationshipUpdateHandler=(core: IDiscordCore, relationship: DiscordRelationship) -> Unit

typealias LobbyUpdateHandler=(core: IDiscordCore, lobbyId: DiscordLobbyId) -> Unit
typealias LobbyDeleteHandler=(core: IDiscordCore, lobbyId: DiscordLobbyId, reason: Int) -> Unit
typealias LobbyMemberConnectHandler=(core: IDiscordCore, lobbyId: DiscordLobbyId, userId: DiscordUserId) -> Unit
typealias LobbyMemberUpdateHandler=(core: IDiscordCore, lobbyId: DiscordLobbyId, userId: DiscordUserId) -> Unit
typealias LobbyMemberDisconnectHandler=(core: IDiscordCore, lobbyId: DiscordLobbyId, userId: DiscordUserId) -> Unit
typealias LobbyMemberMessageHandler=(core: IDiscordCore, lobbyId: Long, userId: Long, data: ByteArray) -> Unit
typealias LobbyMemberSpeakingHandler=(core: IDiscordCore, lobbyId: Long, userId: Long, speaking: Boolean) -> Unit
typealias LobbyNetworkMessageHandler=(core: IDiscordCore, lobbyId: Long, userId: Long, channelId: Int, data: ByteArray) -> Unit

typealias NetworkOnMessageHandler=(core: IDiscordCore, peerId: DiscordNetworkPeerId, channelId: DiscordNetworkChannelId, data: ByteArray) -> Unit
typealias NetworkRouteUpdateHandler=(core: IDiscordCore, route: String) -> Unit

typealias OverlayToggleHandler=(core: IDiscordCore, locked: Boolean) -> Unit

typealias StoreEntitlementCreateHandler=(core: IDiscordCore, entitlement: DiscordEntitlement) -> Unit
typealias StoreEntitlementDeleteHandler=(core: IDiscordCore, entitlement: DiscordEntitlement) -> Unit

typealias AchievementUpdateHandler=(core: IDiscordCore, userAchievement: DiscordUserAchievement) -> Unit