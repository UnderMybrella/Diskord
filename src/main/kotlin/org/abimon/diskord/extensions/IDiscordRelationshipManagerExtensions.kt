package org.abimon.diskord.extensions

import com.sun.jna.ptr.IntByReference
import discordgamesdk.DiscordRelationship
import discordgamesdk.IDiscordRelationshipManager
import org.abimon.diskord.DiscordUserId
import org.abimon.diskord.scopedPointer

fun IDiscordRelationshipManager.filterRelationships(filterOp: (relationshipManager: IDiscordRelationshipManager, relationship: DiscordRelationship) -> Boolean) {
    scopedPointer { ptr ->
        filter.apply(this, ptr) { callbackData, relationship -> if (filterOp(IDiscordRelationshipManager(callbackData), relationship)) 1 else 0 }
    }
}

fun IDiscordRelationshipManager.getRelationship(userId: DiscordUserId): DiscordRelationship =
    DiscordRelationship().also { relationship -> get.apply(this, userId, relationship) }

fun IDiscordRelationshipManager.getRelationshipAt(index: Int): DiscordRelationship =
    DiscordRelationship().also { relationship -> get_at.apply(this, index, relationship) }

fun IDiscordRelationshipManager.count(): Int =
    IntByReference().also { int -> count.apply(this, int) }.value