package org.abimon.diskord.extensions

import discordgamesdk.*
import org.abimon.diskord.scopedPointer

val IDiscordCore.applicationManager: IDiscordApplicationManager
    get() = get_application_manager.apply(this)
val IDiscordCore.userManager: IDiscordUserManager
    get() = get_user_manager.apply(this)
val IDiscordCore.imageManager: IDiscordImageManager
    get() = get_image_manager.apply(this)
val IDiscordCore.activityManager: IDiscordActivityManager
    get() = get_activity_manager.apply(this)
val IDiscordCore.relationshipManager: IDiscordRelationshipManager
    get() = get_relationship_manager.apply(this)
val IDiscordCore.lobbyManager: IDiscordLobbyManager
    get() = get_lobby_manager.apply(this)
val IDiscordCore.networkManager: IDiscordNetworkManager
    get() = get_network_manager.apply(this)
val IDiscordCore.overlayManager: IDiscordOverlayManager
    get() = get_overlay_manager.apply(this)
val IDiscordCore.storageManager: IDiscordStorageManager
    get() = get_storage_manager.apply(this)
val IDiscordCore.storeManager: IDiscordStoreManager
    get() = get_store_manager.apply(this)
val IDiscordCore.voiceManager: IDiscordVoiceManager
    get() = get_voice_manager.apply(this)
val IDiscordCore.achievementManager: IDiscordAchievementManager
    get() = get_achievement_manager.apply(this)

fun IDiscordCore.runCallbacks() = run_callbacks.apply(this)
fun IDiscordCore.destroy() = destroy.apply(this)

fun IDiscordCore.setLogHook(minLevel: Int, callback: (core: IDiscordCore, level: Int, message: String) -> Unit) {
    this.scopedPointer { ptr ->
        set_log_hook.apply(this, minLevel, ptr) { callbackDataPtr, level, msgPtr -> callback(IDiscordCore(callbackDataPtr), level, msgPtr.getString(0)) }
    }
}