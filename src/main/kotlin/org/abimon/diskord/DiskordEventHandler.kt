package org.abimon.diskord

import discordgamesdk.DiscordCreateParams
import discordgamesdk.IDiscordCore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.abimon.diskord.extensions.*

class DiskordEventHandler(val params: DiscordCreateParams) {
    lateinit var core: IDiscordCore
//    val userEventJob = Dispatchers
//    val activityEventJob: Job
//    val relationshipEventJob: Job
//    val lobbyEventJob: Job
//    val networkEventJob: Job
//    val overlayEventJob: Job
//    val storeEventJob: Job
//    val voiceEventJob: Job
//    val achievementEventJob: Job

    val currentUserUpdateHandlers: MutableList<CurrentUserUpdateHandler> = ArrayList()

    val activityJoinHandlers: MutableList<ActivityJoinHandler> = ArrayList()
    val activitySpectateHandlers: MutableList<ActivitySpectateHandler> = ArrayList()
    val activityJoinRequestHandlers: MutableList<ActivityJoinRequestHandler> = ArrayList()
    val activityInviteHandlers: MutableList<ActivityInviteHandler> = ArrayList()

    val relationshipRefreshHandlers: MutableList<RelationshipRefreshHandler> = ArrayList()
    val relationshipUpdateHandlers: MutableList<RelationshipUpdateHandler> = ArrayList()

    val lobbyUpdateHandlers: MutableList<LobbyUpdateHandler> = ArrayList()
    val lobbyDeleteHandlers: MutableList<LobbyDeleteHandler> = ArrayList()
    val lobbyMemberConnectHandlers: MutableList<LobbyMemberConnectHandler> = ArrayList()
    val lobbyMemberUpdateHandlers: MutableList<LobbyMemberUpdateHandler> = ArrayList()
    val lobbyMemberDisconnectHandlers: MutableList<LobbyMemberDisconnectHandler> = ArrayList()
    val lobbyMemberMessageHandlers: MutableList<LobbyMemberMessageHandler> = ArrayList()
    val lobbyMemberSpeakingHandlers: MutableList<LobbyMemberSpeakingHandler> = ArrayList()
    val lobbyNetworkMessageHandlers: MutableList<LobbyNetworkMessageHandler> = ArrayList()

    val networkOnMessageHandlers: MutableList<NetworkOnMessageHandler> = ArrayList()
    val networkRouteUpdateHandlers: MutableList<NetworkRouteUpdateHandler> = ArrayList()

    val overlayToggleHandlers: MutableList<OverlayToggleHandler> = ArrayList()

    val storeEntitlementCreateHandlers: MutableList<StoreEntitlementCreateHandler> = ArrayList()
    val storeEntitlementDeleteHandlers: MutableList<StoreEntitlementDeleteHandler> = ArrayList()

    val achievementUpdateHandlers: MutableList<AchievementUpdateHandler> = ArrayList()

    init {
        params.userEvents = userEvents {
            onCurrentUserUpdate = onCurrentUserUpdate {
                GlobalScope.launch { currentUserUpdateHandlers.forEach { handle -> handle(core) } }
            }
        }
        params.activityEvents = activityEvents {
            onActivityJoin = onActivityJoin { _, secretPointer ->
                val secret = secretPointer.readUTF8()
                GlobalScope.launch { activityJoinHandlers.forEach { handler -> handler(core, secret) } }
            }

            onActivitySpectate = onActivitySpectate { _, secretPointer ->
                val secret = secretPointer.readUTF8()
                GlobalScope.launch { activitySpectateHandlers.forEach { handler -> handler(core, secret) } }
            }

            onActivityJoinRequest = onActivityJoinRequest { _, user ->
                GlobalScope.launch { activityJoinRequestHandlers.forEach { handler -> handler(core, user) } }
            }

            onActivityInvite = onActivityInvite { _, type, user, activity ->
                GlobalScope.launch { activityInviteHandlers.forEach { handler -> handler(core, type, user, activity) } }
            }
        }
        params.relationshipEvents = relationshipEvents {
            onRefresh =
                onRefresh { GlobalScope.launch { relationshipRefreshHandlers.forEach { handler -> handler(core) } } }

            onRelationshipUpdate = onRelationshipUpdate { _, relationship ->
                GlobalScope.launch { relationshipUpdateHandlers.forEach { handle -> handle(core, relationship) } }
            }
        }
        params.lobbyEvents = lobbyEvents {
            onLobbyUpdate = onLobbyUpdate { _, lobbyId ->
                GlobalScope.launch { lobbyUpdateHandlers.forEach { handle -> handle(core, lobbyId) } }
            }
            onLobbyDelete = onLobbyDelete { _, lobbyId, reason ->
                GlobalScope.launch { lobbyDeleteHandlers.forEach { handle -> handle(core, lobbyId, reason) } }
            }
            onMemberConnect = onMemberConnect { _, lobbyId, userId -> 
                GlobalScope.launch { lobbyMemberConnectHandlers.forEach { handle -> handle(core, lobbyId, userId) } }
            }
            onMemberUpdate = onMemberUpdate { _, lobbyId, userId -> 
                GlobalScope.launch { lobbyMemberUpdateHandlers.forEach { handle -> handle(core, lobbyId, userId) } }
            }
            onMemberDisconnect = onMemberDisconnect { _, lobbyId, userId ->
                GlobalScope.launch { lobbyMemberDisconnectHandlers.forEach { handle -> handle(core, lobbyId, userId) } }
            }
            onLobbyMessage = onLobbyMessage { _, lobbyId, userId, data, dataLength ->
                val message = data.getByteArray(0, dataLength)
                GlobalScope.launch { lobbyMemberMessageHandlers.forEach { handle -> handle(core, lobbyId, userId, message) } }
            }
            onSpeaking = onSpeaking { _, lobbyId, userId, speaking ->
                GlobalScope.launch { lobbyMemberSpeakingHandlers.forEach { handle -> handle(core, lobbyId, userId, speaking.toInt() != 0) } }
            }
            onNetworkMessage = onNetworkMessage { _, lobbyId, userId, channelId, data, dataLength ->
                val message = data.getByteArray(0, dataLength)
                GlobalScope.launch { lobbyNetworkMessageHandlers.forEach { handle -> handle(core, lobbyId, userId, channelId.toInt(), message) } }
            }
        }
        params.networkEvents = networkEvents {
            onMessage = onMessage { _, peerId, channelId, data, dataLength ->
                val message = data.getByteArray(0, dataLength)
                GlobalScope.launch { networkOnMessageHandlers.forEach { handle -> handle(core, peerId.toULong(), channelId.toUByte(), message) } }
            }
            onRouteUpdate = onRouteUpdate { _, routeData ->
                val route = routeData.readUTF8()
                GlobalScope.launch { networkRouteUpdateHandlers.forEach { handle -> handle(core, route) }}
            }
        }
        params.overlayEvents = overlayEvents {
            onToggle = onToggle { _, locked ->
                val isLocked = locked.toInt() > 0
                GlobalScope.launch { overlayToggleHandlers.forEach { handle -> handle(core, isLocked) } }
            }
        }
        params.storeEvents = storeEvents {
            onEntitlementCreate = onEntitlementCreate { _, entitlement ->
                GlobalScope.launch { storeEntitlementCreateHandlers.forEach { handle -> handle(core, entitlement) } }
            }
            onEntitlementDelete = onEntitlementDelete { _, entitlement ->
                GlobalScope.launch { storeEntitlementDeleteHandlers.forEach { handle -> handle(core, entitlement) } }
            }
        }
        params.achievementEvents = achievementEvents {
            onUserAchievementUpdate = onUserAchievementUpdate { _, userAchievement ->
                GlobalScope.launch { achievementUpdateHandlers.forEach { handle -> handle(core, userAchievement) } }
            }
        }
    }
}