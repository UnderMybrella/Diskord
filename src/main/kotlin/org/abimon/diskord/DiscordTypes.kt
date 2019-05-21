package org.abimon.diskord

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
@ExperimentalUnsignedTypes
typealias DiscordNetworkPeerId = ULong
@ExperimentalUnsignedTypes
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