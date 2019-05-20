package org.abimon.diskord

import discordgamesdk.DiscordActivity
import discordgamesdk.DiscordActivityAssets
import discordgamesdk.DiscordActivityParty
import discordgamesdk.DiscordActivitySecrets
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty

var DiscordActivity.stateAsString: String by byteArray(DiscordActivity::state)
var DiscordActivity.detailsAsString: String by byteArray(DiscordActivity::details)
var DiscordActivity.nameAsString: String by byteArray(DiscordActivity::name)

var DiscordActivityParty.idAsString: String by byteArray(DiscordActivityParty::id)

var DiscordActivitySecrets.joinAsString: String by byteArray(DiscordActivitySecrets::join)
var DiscordActivitySecrets.matchAsString: String by byteArray(DiscordActivitySecrets::match)
var DiscordActivitySecrets.spectateAsString: String by byteArray(DiscordActivitySecrets::spectate)

var DiscordActivityAssets.largeImageAsString: String by byteArray(DiscordActivityAssets::large_image)
var DiscordActivityAssets.largeTextAsString: String by byteArray(DiscordActivityAssets::large_text)
var DiscordActivityAssets.smallImageAsString: String by byteArray(DiscordActivityAssets::small_image)
var DiscordActivityAssets.smallTextAsString: String by byteArray(DiscordActivityAssets::small_text)

fun <T> byteArray(byteArrayProperty: KMutableProperty1<T, ByteArray>): ReadWriteProperty<T, String> = object: ReadWriteProperty<T, String> {
    override fun getValue(thisRef: T, property: KProperty<*>): String = String(byteArrayProperty.get(thisRef), Charsets.UTF_8).substringBefore('\u0000')

    override fun setValue(thisRef: T, property: KProperty<*>, value: String) {
        val array = byteArrayProperty.get(thisRef)
        Arrays.fill(array, 0)
        value.toByteArray(Charsets.UTF_8).copyInto(array)
        byteArrayProperty.set(thisRef, array)
    }

}