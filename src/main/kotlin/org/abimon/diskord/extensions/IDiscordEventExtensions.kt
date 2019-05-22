package org.abimon.diskord.extensions

import com.sun.jna.Pointer
import discordgamesdk.*
import org.abimon.diskord.DiscordLobbyId
import org.abimon.diskord.DiscordUserId
import org.abimon.diskord.getValue
import org.abimon.diskord.setValue

var DiscordCreateParams.eventData by DiscordCreateParams::event_data
var DiscordCreateParams.applicationEvents by DiscordCreateParams::application_events
var DiscordCreateParams.userEvents by DiscordCreateParams::user_events
var DiscordCreateParams.imageEvents by DiscordCreateParams::image_events
var DiscordCreateParams.activityEvents by DiscordCreateParams::activity_events
var DiscordCreateParams.relationshipEvents by DiscordCreateParams::relationship_events
var DiscordCreateParams.lobbyEvents by DiscordCreateParams::lobby_events
var DiscordCreateParams.networkEvents by DiscordCreateParams::network_events
var DiscordCreateParams.overlayEvents by DiscordCreateParams::overlay_events
var DiscordCreateParams.storageEvents by DiscordCreateParams::storage_events
var DiscordCreateParams.storeEvents by DiscordCreateParams::store_events
var DiscordCreateParams.voiceEvents by DiscordCreateParams::voice_events
var DiscordCreateParams.achievementEvents by DiscordCreateParams::achievement_events

var IDiscordUserEvents.onCurrentUserUpdate by IDiscordUserEvents::on_current_user_update

fun IDiscordUserEvents.onCurrentUserUpdate(callback: (eventData: Pointer) -> Unit) =
        IDiscordUserEvents.on_current_user_update_callback(callback)

fun userEvents(init: IDiscordUserEvents.ByReference.() -> Unit): IDiscordUserEvents.ByReference {
    val userEvents = IDiscordUserEvents.ByReference()
    userEvents.init()
    return userEvents
}

var IDiscordActivityEvents.onActivityJoin by IDiscordActivityEvents::on_activity_join
var IDiscordActivityEvents.onActivitySpectate by IDiscordActivityEvents::on_activity_spectate
var IDiscordActivityEvents.onActivityJoinRequest by IDiscordActivityEvents::on_activity_join_request
var IDiscordActivityEvents.onActivityInvite by IDiscordActivityEvents::on_activity_invite

fun IDiscordActivityEvents.onActivityJoin(callback: (eventData: Pointer, secret: Pointer) -> Unit) =
    IDiscordActivityEvents.on_activity_join_callback(callback)

fun IDiscordActivityEvents.onActivitySpectate(callback: (eventData: Pointer, secret: Pointer) -> Unit) =
    IDiscordActivityEvents.on_activity_spectate_callback(callback)

fun IDiscordActivityEvents.onActivityJoinRequest(callback: (eventData: Pointer, user: DiscordUser) -> Unit) =
    IDiscordActivityEvents.on_activity_join_request_callback(callback)

fun IDiscordActivityEvents.onActivityInvite(callback: (eventData: Pointer, type: Int, user: DiscordUser, activity: DiscordActivity) -> Unit) =
    IDiscordActivityEvents.on_activity_invite_callback(callback)

fun activityEvents(init: IDiscordActivityEvents.ByReference.() -> Unit): IDiscordActivityEvents.ByReference {
    val activityEvents = IDiscordActivityEvents.ByReference()
    activityEvents.init()
    return activityEvents
}

var IDiscordRelationshipEvents.onRefresh by IDiscordRelationshipEvents::on_refresh
var IDiscordRelationshipEvents.onRelationshipUpdate by IDiscordRelationshipEvents::on_relationship_update

fun IDiscordRelationshipEvents.onRefresh(callback: (eventData: Pointer) -> Unit) =
    IDiscordRelationshipEvents.on_refresh_callback(callback)

fun IDiscordRelationshipEvents.onRelationshipUpdate(callback: (eventData: Pointer, relationship: DiscordRelationship) -> Unit) =
    IDiscordRelationshipEvents.on_relationship_update_callback(callback)

fun relationshipEvents(init: IDiscordRelationshipEvents.ByReference.() -> Unit): IDiscordRelationshipEvents.ByReference {
    val relationshipEvents = IDiscordRelationshipEvents.ByReference()
    relationshipEvents.init()
    return relationshipEvents
}

var IDiscordLobbyEvents.onLobbyUpdate by IDiscordLobbyEvents::on_lobby_update
var IDiscordLobbyEvents.onLobbyDelete by IDiscordLobbyEvents::on_lobby_delete
var IDiscordLobbyEvents.onMemberConnect by IDiscordLobbyEvents::on_member_connect
var IDiscordLobbyEvents.onMemberUpdate by IDiscordLobbyEvents::on_member_update
var IDiscordLobbyEvents.onMemberDisconnect by IDiscordLobbyEvents::on_member_disconnect
var IDiscordLobbyEvents.onLobbyMessage by IDiscordLobbyEvents::on_lobby_message
var IDiscordLobbyEvents.onSpeaking by IDiscordLobbyEvents::on_speaking
var IDiscordLobbyEvents.onNetworkMessage by IDiscordLobbyEvents::on_network_message

fun IDiscordLobbyEvents.onLobbyUpdate(callback: (eventData: Pointer, lobbyId: DiscordLobbyId) -> Unit) =
    IDiscordLobbyEvents.on_lobby_update_callback(callback)

fun IDiscordLobbyEvents.onLobbyDelete(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, reason: Int) -> Unit) =
    IDiscordLobbyEvents.on_lobby_delete_callback(callback)

fun IDiscordLobbyEvents.onMemberConnect(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, userId: DiscordUserId) -> Unit) =
    IDiscordLobbyEvents.on_member_connect_callback(callback)

fun IDiscordLobbyEvents.onMemberUpdate(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, userId: DiscordUserId) -> Unit) =
    IDiscordLobbyEvents.on_member_update_callback(callback)

fun IDiscordLobbyEvents.onMemberDisconnect(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, userId: DiscordUserId) -> Unit) =
    IDiscordLobbyEvents.on_member_disconnect_callback(callback)

fun IDiscordLobbyEvents.onLobbyMessage(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, userId: DiscordUserId, data: Pointer, dataLength: Int) -> Unit) =
    IDiscordLobbyEvents.on_lobby_message_callback(callback)

fun IDiscordLobbyEvents.onSpeaking(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, userId: DiscordUserId, speaking: Byte) -> Unit) =
    IDiscordLobbyEvents.on_speaking_callback(callback)

fun IDiscordLobbyEvents.onNetworkMessage(callback: (eventData: Pointer, lobbyId: DiscordLobbyId, userId: DiscordUserId, channelId: Byte, data: Pointer, dataLength: Int) -> Unit) =
    IDiscordLobbyEvents.on_network_message_callback(callback)

fun lobbyEvents(init: IDiscordLobbyEvents.ByReference.() -> Unit): IDiscordLobbyEvents.ByReference {
    val lobbyEvents = IDiscordLobbyEvents.ByReference()
    lobbyEvents.init()
    return lobbyEvents
}

var IDiscordNetworkEvents.onMessage by IDiscordNetworkEvents::on_message
var IDiscordNetworkEvents.onRouteUpdate by IDiscordNetworkEvents::on_route_update

fun IDiscordNetworkEvents.onMessage(callback: (eventData: Pointer, peerId: Long, channelId: Byte, data: Pointer, dataLength: Int) -> Unit) =
    IDiscordNetworkEvents.on_message_callback(callback)

fun IDiscordNetworkEvents.onRouteUpdate(callback: (eventData: Pointer, routeData: Pointer) -> Unit) =
    IDiscordNetworkEvents.on_route_update_callback(callback)

fun networkEvents(init: IDiscordNetworkEvents.ByReference.() -> Unit): IDiscordNetworkEvents.ByReference {
    val networkEvents = IDiscordNetworkEvents.ByReference()
    networkEvents.init()
    return networkEvents
}

var IDiscordOverlayEvents.onToggle by IDiscordOverlayEvents::on_toggle

fun IDiscordOverlayEvents.onToggle(callback: (eventData: Pointer, locked: Byte) -> Unit) =
    IDiscordOverlayEvents.on_toggle_callback(callback)

fun overlayEvents(init: IDiscordOverlayEvents.ByReference.() -> Unit): IDiscordOverlayEvents.ByReference {
    val overlayEvents = IDiscordOverlayEvents.ByReference()
    overlayEvents.init()
    return overlayEvents
}

var IDiscordStoreEvents.onEntitlementCreate by IDiscordStoreEvents::on_entitlement_create
var IDiscordStoreEvents.onEntitlementDelete by IDiscordStoreEvents::on_entitlement_delete

fun IDiscordStoreEvents.onEntitlementCreate(callback: (eventData: Pointer, entitlement: DiscordEntitlement) -> Unit) =
    IDiscordStoreEvents.on_entitlement_create_callback(callback)

fun IDiscordStoreEvents.onEntitlementDelete(callback: (eventData: Pointer, entitlement: DiscordEntitlement) -> Unit) =
    IDiscordStoreEvents.on_entitlement_delete_callback(callback)

fun storeEvents(init: IDiscordStoreEvents.ByReference.() -> Unit): IDiscordStoreEvents.ByReference {
    val storeEvents = IDiscordStoreEvents.ByReference()
    storeEvents.init()
    return storeEvents
}

var IDiscordVoiceEvents.onSettingsUpdate by IDiscordVoiceEvents::on_settings_update

fun IDiscordVoiceEvents.onSettingsUpdate(callback: (eventData: Pointer) -> Unit) =
    IDiscordVoiceEvents.on_settings_update_callback(callback)

fun voiceEvents(init: IDiscordVoiceEvents.ByReference.() -> Unit): IDiscordVoiceEvents.ByReference {
    val voiceEvents = IDiscordVoiceEvents.ByReference()
    voiceEvents.init()
    return voiceEvents
}

var IDiscordAchievementEvents.onUserAchievementUpdate by IDiscordAchievementEvents::on_user_achievement_update

fun IDiscordAchievementEvents.onUserAchievementUpdate(callback: (eventData: Pointer, userAchievement: DiscordUserAchievement) -> Unit) =
    IDiscordAchievementEvents.on_user_achievement_update_callback(callback)

fun achievementEvents(init: IDiscordAchievementEvents.ByReference.() -> Unit): IDiscordAchievementEvents.ByReference {
    val achievementEvents = IDiscordAchievementEvents.ByReference()
    achievementEvents.init()
    return achievementEvents
}