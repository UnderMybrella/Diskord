package org.abimon.diskord.extensions

import com.sun.jna.ptr.LongByReference
import discordgamesdk.IDiscordNetworkManager
import org.abimon.diskord.DiscordNetworkPeerId
import org.abimon.diskord.DiscordResult
import org.abimon.diskord.memScoped

val IDiscordNetworkManager.peerId: DiscordNetworkPeerId
    get() = LongByReference().also { ref -> get_peer_id.apply(this, ref) }.value.toULong()

fun IDiscordNetworkManager.flush(): DiscordResult<Unit> =
        DiscordResult(flush.apply(this))

fun IDiscordNetworkManager.openChannel(peerId: DiscordNetworkPeerId, channelId: Int, reliable: Boolean) =
        DiscordResult(open_channel.apply(this, peerId.toLong(), channelId.toByte(), if (reliable) 1 else 0))

fun IDiscordNetworkManager.openPeer(peerId: DiscordNetworkPeerId, route: String) =
        memScoped(route) { mem -> DiscordResult(open_peer.apply(this, peerId.toLong(), mem)) } //Dunno if this expects a specific size

fun IDiscordNetworkManager.updatePeer(peerId: DiscordNetworkPeerId, route: String) =
    memScoped(route) { mem -> DiscordResult(update_peer.apply(this, peerId.toLong(), mem)) } //Dunno if this expects a specific size

fun IDiscordNetworkManager.sendMessage(peerId: DiscordNetworkPeerId, channelId: Int, message: String) = sendMessage(peerId, channelId, message.toByteArray(Charsets.UTF_8))
fun IDiscordNetworkManager.sendMessage(peerId: DiscordNetworkPeerId, channelId: Int, data: ByteArray) =
        memScoped(data) { mem -> DiscordResult(send_message.apply(this, peerId.toLong(), channelId.toByte(), mem, mem.size().toInt())) }

fun IDiscordNetworkManager.closeCHannel(peerId: DiscordNetworkPeerId, channelId: Int) =
        DiscordResult(close_channel.apply(this, peerId.toLong(), channelId.toByte()))

fun IDiscordNetworkManager.closePeer(peerId: DiscordNetworkPeerId) =
        DiscordResult(close_peer.apply(this, peerId.toLong()))