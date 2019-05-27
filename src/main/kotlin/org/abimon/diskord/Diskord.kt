package org.abimon.diskord

import discordgamesdk.DiscordCreateParams
import discordgamesdk.DiscordGameSDKLibrary
import discordgamesdk.DiscordGameSDKLibrary.EDiscordCreateFlags.DiscordCreateFlags_Default
import discordgamesdk.IDiscordCore
import kotlinx.coroutines.*
import org.abimon.diskord.extensions.*
import java.util.concurrent.Executors

object Diskord : DiscordGameSDKLibrary by DiscordGameSDKLibrary.INSTANCE {
    var DEFAULT_TIMEOUT = 5000L
}

class DiscordCoreRef {
    val listeners: MutableList<(IDiscordCore) -> Unit> = ArrayList()
    private lateinit var _core: IDiscordCore
    var core: IDiscordCore
        get() = _core
        set(value) {
            _core = value
            listeners.forEach { handle -> handle(value) }
        }
}

fun createSDK(createJob: Boolean = true, paramInit: DiscordCreateParams.(DiscordCoreRef) -> Unit): IDiscordCore {
    val coreRef = DiscordCoreRef()
    val ptr = arrayOf(IDiscordCore.ByReference())

    val params = DiscordCreateParams()
    params.flags = DiscordCreateFlags_Default.toLong()
    params.paramInit(coreRef)

    val response = Diskord.DiscordCreate(DiscordGameSDKLibrary.DISCORD_VERSION, params, ptr)
    require(response == DiscordGameSDKLibrary.EDiscordResult.DiscordResult_Ok) { "Failed to start up Diskord" }
    coreRef.core = ptr[0]
    val core = ptr[0]

    if (createJob) {
        GlobalScope.launch(Executors.newSingleThreadExecutor {
            Thread(it).apply {
                isDaemon = true
            }
        }.asCoroutineDispatcher()) {
            launch {
                while (isActive) {
                    delay(20)
                    val result = core.run_callbacks.apply(core)
                    if (result != DiscordGameSDKLibrary.EDiscordResult.DiscordResult_Ok)
                        System.err.println(
                            "Callback failed (${DiscordResult.FailResult<Int>(result)::class.java.simpleName.substringAfter(
                                "FailResult"
                            )})"
                        )
//                core.lobbyManager.flushNetwork()
//                    .orElse { failRes ->
//                        System.err.println(
//                            "Flush failed (${failRes::class.java.simpleName.substringAfter(
//                                "FailResult"
//                            )}"
//                        )
//                    }
                }
            }
        }
    }

    core.setLogHook(DiscordGameSDKLibrary.EDiscordLogLevel.DiscordLogLevel_Debug) { _, level, msg ->
        when (level) {
            DiscordGameSDKLibrary.EDiscordLogLevel.DiscordLogLevel_Warn -> println("WARN: $msg")
            DiscordGameSDKLibrary.EDiscordLogLevel.DiscordLogLevel_Info -> println("INFO: $msg")
            DiscordGameSDKLibrary.EDiscordLogLevel.DiscordLogLevel_Error -> println("ERROR: $msg")
            DiscordGameSDKLibrary.EDiscordLogLevel.DiscordLogLevel_Debug -> println("DEBUG: $msg")
        }
    }

    core.achievementManager
    core.activityManager
    core.applicationManager
    core.imageManager
    core.lobbyManager
    core.networkManager
    core.overlayManager
    core.relationshipManager
    core.storageManager
    core.storeManager
    core.userManager
    core.voiceManager

    return core
}

fun DiscordCreateParams.withEvents(ref: DiscordCoreRef? = null, init: DiskordEventHandler.() -> Unit): DiskordEventHandler {
    val eventHandler = DiskordEventHandler(this)
    eventHandler.init()
    ref?.listeners?.add { core -> eventHandler.core = core }
    return eventHandler
}