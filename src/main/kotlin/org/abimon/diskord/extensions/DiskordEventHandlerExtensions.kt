package org.abimon.diskord.extensions

import org.abimon.diskord.*

fun DiskordEventHandler.onCurrentUserUpdate(handler: CurrentUserUpdateHandler) =
    currentUserUpdateHandlers.add(handler)


fun DiskordEventHandler.onActivityJoin(handler: ActivityJoinHandler) =
    activityJoinHandlers.add(handler)

fun DiskordEventHandler.onActivitySpectate(handler: ActivitySpectateHandler) =
    activitySpectateHandlers.add(handler)

fun DiskordEventHandler.onActivityJoinRequest(handler: ActivityJoinRequestHandler) =
    activityJoinRequestHandlers.add(handler)

fun DiskordEventHandler.onActivityInvite(handler: ActivityInviteHandler) =
    activityInviteHandlers.add(handler)


fun DiskordEventHandler.onRelationshipRefresh(handler: RelationshipRefreshHandler) =
        relationshipRefreshHandlers.add(handler)

fun DiskordEventHandler.onRelationshipUpdate(handler: RelationshipUpdateHandler) =
        relationshipUpdateHandlers.add(handler)

fun DiskordEventHandler.onLobbyUpdate(handler: LobbyUpdateHandler) =
        lobbyUpdateHandlers.add(handler)
fun DiskordEventHandler.onLobbyDelete(handler: LobbyDeleteHandler) =
        lobbyDeleteHandlers.add(handler)
fun DiskordEventHandler.onLobbyMemberConnect(handler: LobbyMemberConnectHandler) =
        lobbyMemberConnectHandlers.add(handler)
fun DiskordEventHandler.onLobbyMemberUpdate(handler: LobbyMemberUpdateHandler) =
        lobbyMemberUpdateHandlers.add(handler)
fun DiskordEventHandler.onLobbyMemberDisconnect(handler: LobbyMemberDisconnectHandler) =
        lobbyMemberDisconnectHandlers.add(handler)
fun DiskordEventHandler.onLobbyMemberMessage(handler: LobbyMemberMessageHandler) =
        lobbyMemberMessageHandlers.add(handler)
fun DiskordEventHandler.onLobbySpeaking(handler: LobbyMemberSpeakingHandler) =
        lobbyMemberSpeakingHandlers.add(handler)
fun DiskordEventHandler.onLobbyNetworkMessage(handler: LobbyNetworkMessageHandler) =
        lobbyNetworkMessageHandlers.add(handler)

fun DiskordEventHandler.onNetworkMessage(handler: NetworkOnMessageHandler) =
        networkOnMessageHandlers.add(handler)
fun DiskordEventHandler.onNetworkRouteUpdate(handler: NetworkRouteUpdateHandler) =
        networkRouteUpdateHandlers.add(handler)

fun DiskordEventHandler.onOverlayToggle(handler: OverlayToggleHandler) =
        overlayToggleHandlers.add(handler)

fun DiskordEventHandler.onStoreEntitlementCreate(handler: StoreEntitlementCreateHandler) =
        storeEntitlementCreateHandlers.add(handler)
fun DiskordEventHandler.onStoreEntitlementDelete(handler: StoreEntitlementDeleteHandler) =
        storeEntitlementDeleteHandlers.add(handler)

fun DiskordEventHandler.onUserAchievementUpdate(handler: AchievementUpdateHandler) =
        achievementUpdateHandlers.add(handler)