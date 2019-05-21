package org.abimon.diskord

import discordgamesdk.DiscordGameSDKLibrary.EDiscordResult.*

sealed class DiscordResult<T> {
    companion object {
        operator fun <T> invoke(result: Int, data: T?): DiscordResult<T> = if (result == DiscordResult_Ok && data != null) Success(data, result) else FailResult(result)
        operator fun invoke(result: Int): DiscordResult<Unit> = this(result, Unit)
    }
    data class Success<T>(val data: T, val result: Int): DiscordResult<T>() {
        override fun ifPresent(func: (T) -> Unit): DiscordResult<T> {
            func(data)
            return this
        }
        override fun <R> map(func: (T) -> R): DiscordResult<R> = Success(func(data), result)
        override fun <R> flatMap(func: (T) -> DiscordResult<R>): DiscordResult<R> = func(data)
        override fun filter(func: (T) -> Boolean): DiscordResult<T> = if (func(data)) this else FilterFail()

        override suspend fun ifPresentSuspend(func: suspend (T) -> Unit): DiscordResult<T> {
            func(data)
            return this
        }
        override suspend fun <R> mapSuspend (func: suspend (T) -> R): DiscordResult<R> = Success(func(data), result)
        override suspend fun <R> flatMapSuspend (func: suspend (T) -> DiscordResult<R>): DiscordResult<R> = func(data)
        override suspend fun filterSuspend (func: suspend (T) -> Boolean): DiscordResult<T> = if (func(data)) this else FilterFail()

        override fun orElse(t: T): T = data
        override fun orElse(supplier: (DiscordResult<T>) -> T): T = data
        override fun orNull(): T? = data
    }
    @Suppress("UNCHECKED_CAST")
    abstract class Fail<T>: DiscordResult<T>() {
        override fun ifPresent(func: (T) -> Unit): DiscordResult<T> = this
        override fun <R> map(func: (T) -> R): DiscordResult<R> = this as DiscordResult<R>
        override fun <R> flatMap(func: (T) -> DiscordResult<R>): DiscordResult<R> = this as DiscordResult<R>
        override fun filter(func: (T) -> Boolean): DiscordResult<T> = this

        override suspend fun ifPresentSuspend(func: suspend (T) -> Unit): DiscordResult<T> = this
        override suspend fun <R> mapSuspend (func: suspend (T) -> R): DiscordResult<R> = this as DiscordResult<R>
        override suspend fun <R> flatMapSuspend (func: suspend (T) -> DiscordResult<R>): DiscordResult<R> = this as DiscordResult<R>
        override suspend fun filterSuspend (func: suspend (T) -> Boolean): DiscordResult<T> = this

        override fun orElse(t: T): T = t
        override fun orElse(supplier: (DiscordResult<T>) -> T): T = supplier(this)
        override fun orNull(): T? = null
    }

    abstract class FailResult<T> protected constructor(val result: Int): Fail<T>() {
        companion object {
            operator fun <T> invoke(result: Int): FailResult<T> =
                when (result) {
                    DiscordResult_ServiceUnavailable -> FailResultServiceUnavailable()
                    DiscordResult_InvalidVersion -> FailResultInvalidVersion()
                    DiscordResult_LockFailed -> FailResultLockFailed()
                    DiscordResult_InternalError -> FailResultInternalError()
                    DiscordResult_InvalidPayload -> FailResultInvalidPayload()
                    DiscordResult_InvalidCommand -> FailResultInvalidCommand()
                    DiscordResult_InvalidPermissions -> FailResultInvalidPermissions()
                    DiscordResult_NotFetched -> FailResultNotFetched()
                    DiscordResult_NotFound -> FailResultNotFound()
                    DiscordResult_Conflict -> FailResultConflict()
                    DiscordResult_InvalidSecret -> FailResultInvalidSecret()
                    DiscordResult_InvalidJoinSecret -> FailResultInvalidJoinSecret()
                    DiscordResult_NoEligibleActivity -> FailResultNoEligibleActivity()
                    DiscordResult_InvalidInvite -> FailResultInvalidInvite()
                    DiscordResult_NotAuthenticated -> FailResultNotAuthenticated()
                    DiscordResult_InvalidAccessToken -> FailResultInvalidAccessToken()
                    DiscordResult_ApplicationMismatch -> FailResultApplicationMismatch()
                    DiscordResult_InvalidDataUrl -> FailResultInvalidDataUrl()
                    DiscordResult_InvalidBase64 -> FailResultInvalidBase64()
                    DiscordResult_NotFiltered -> FailResultNotFiltered()
                    DiscordResult_LobbyFull -> FailResultLobbyFull()
                    DiscordResult_InvalidLobbySecret -> FailResultInvalidLobbySecret()
                    DiscordResult_InvalidFilename -> FailResultInvalidFilename()
                    DiscordResult_InvalidFileSize -> FailResultInvalidFileSize()
                    DiscordResult_InvalidEntitlement -> FailResultInvalidEntitlement()
                    DiscordResult_NotInstalled -> FailResultNotInstalled()
                    DiscordResult_NotRunning -> FailResultNotRunning()
                    DiscordResult_InsufficientBuffer -> FailResultInsufficientBuffer()
                    DiscordResult_PurchaseCanceled -> FailResultPurchaseCanceled()
                    DiscordResult_InvalidGuild -> FailResultInvalidGuild()
                    DiscordResult_InvalidEvent -> FailResultInvalidEvent()
                    DiscordResult_InvalidChannel -> FailResultInvalidChannel()
                    DiscordResult_InvalidOrigin -> FailResultInvalidOrigin()
                    DiscordResult_RateLimited -> FailResultRateLimited()
                    DiscordResult_OAuth2Error -> FailResultOAuth2Error()
                    DiscordResult_SelectChannelTimeout -> FailResultSelectChannelTimeout()
                    DiscordResult_GetGuildTimeout -> FailResultGetGuildTimeout()
                    DiscordResult_SelectVoiceForceRequired -> FailResultSelectVoiceForceRequired()
                    DiscordResult_CaptureShortcutAlreadyListening -> FailResultCaptureShortcutAlreadyListening()
                    DiscordResult_UnauthorizedForAchievement -> FailResultUnauthorizedForAchievement()
                    DiscordResult_InvalidGiftCode -> FailResultInvalidGiftCode()
                    DiscordResult_PurchaseError -> FailResultPurchaseError()
                    DiscordResult_TransactionAborted -> FailResultTransactionAborted()
                    else -> FailResultOther(result)
                }
        }
        operator fun component1(): Int = result
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FailResult<*>) return false
            if (result != other.result) return false
            return true
        }
        override fun hashCode(): Int {
            return result
        }

    }
    class FailResultServiceUnavailable<T>: FailResult<T>(DiscordResult_ServiceUnavailable)
    class FailResultInvalidVersion<T>: FailResult<T>(DiscordResult_InvalidVersion)
    class FailResultLockFailed<T>: FailResult<T>(DiscordResult_LockFailed)
    class FailResultInternalError<T>: FailResult<T>(DiscordResult_InternalError)
    class FailResultInvalidPayload<T>: FailResult<T>(DiscordResult_InvalidPayload)
    class FailResultInvalidCommand<T>: FailResult<T>(DiscordResult_InvalidCommand)
    class FailResultInvalidPermissions<T>: FailResult<T>(DiscordResult_InvalidPermissions)
    class FailResultNotFetched<T>: FailResult<T>(DiscordResult_NotFetched)
    class FailResultNotFound<T>: FailResult<T>(DiscordResult_NotFound)
    class FailResultConflict<T>: FailResult<T>(DiscordResult_Conflict)
    class FailResultInvalidSecret<T>: FailResult<T>(DiscordResult_InvalidSecret)
    class FailResultInvalidJoinSecret<T>: FailResult<T>(DiscordResult_InvalidJoinSecret)
    class FailResultNoEligibleActivity<T>: FailResult<T>(DiscordResult_NoEligibleActivity)
    class FailResultInvalidInvite<T>: FailResult<T>(DiscordResult_InvalidInvite)
    class FailResultNotAuthenticated<T>: FailResult<T>(DiscordResult_NotAuthenticated)
    class FailResultInvalidAccessToken<T>: FailResult<T>(DiscordResult_InvalidAccessToken)
    class FailResultApplicationMismatch<T>: FailResult<T>(DiscordResult_ApplicationMismatch)
    class FailResultInvalidDataUrl<T>: FailResult<T>(DiscordResult_InvalidDataUrl)
    class FailResultInvalidBase64<T>: FailResult<T>(DiscordResult_InvalidBase64)
    class FailResultNotFiltered<T>: FailResult<T>(DiscordResult_NotFiltered)
    class FailResultLobbyFull<T>: FailResult<T>(DiscordResult_LobbyFull)
    class FailResultInvalidLobbySecret<T>: FailResult<T>(DiscordResult_InvalidLobbySecret)
    class FailResultInvalidFilename<T>: FailResult<T>(DiscordResult_InvalidFilename)
    class FailResultInvalidFileSize<T>: FailResult<T>(DiscordResult_InvalidFileSize)
    class FailResultInvalidEntitlement<T>: FailResult<T>(DiscordResult_InvalidEntitlement)
    class FailResultNotInstalled<T>: FailResult<T>(DiscordResult_NotInstalled)
    class FailResultNotRunning<T>: FailResult<T>(DiscordResult_NotRunning)
    class FailResultInsufficientBuffer<T>: FailResult<T>(DiscordResult_InsufficientBuffer)
    class FailResultPurchaseCanceled<T>: FailResult<T>(DiscordResult_PurchaseCanceled)
    class FailResultInvalidGuild<T>: FailResult<T>(DiscordResult_InvalidGuild)
    class FailResultInvalidEvent<T>: FailResult<T>(DiscordResult_InvalidEvent)
    class FailResultInvalidChannel<T>: FailResult<T>(DiscordResult_InvalidChannel)
    class FailResultInvalidOrigin<T>: FailResult<T>(DiscordResult_InvalidOrigin)
    class FailResultRateLimited<T>: FailResult<T>(DiscordResult_RateLimited)
    class FailResultOAuth2Error<T>: FailResult<T>(DiscordResult_OAuth2Error)
    class FailResultSelectChannelTimeout<T>: FailResult<T>(DiscordResult_SelectChannelTimeout)
    class FailResultGetGuildTimeout<T>: FailResult<T>(DiscordResult_GetGuildTimeout)
    class FailResultSelectVoiceForceRequired<T>: FailResult<T>(DiscordResult_SelectVoiceForceRequired)
    class FailResultCaptureShortcutAlreadyListening<T>: FailResult<T>(DiscordResult_CaptureShortcutAlreadyListening)
    class FailResultUnauthorizedForAchievement<T>: FailResult<T>(DiscordResult_UnauthorizedForAchievement)
    class FailResultInvalidGiftCode<T>: FailResult<T>(DiscordResult_InvalidGiftCode)
    class FailResultPurchaseError<T>: FailResult<T>(DiscordResult_PurchaseError)
    class FailResultTransactionAborted<T>: FailResult<T>(DiscordResult_TransactionAborted)
    class FailResultOther<T>(result: Int): FailResult<T>(result)

    class TimeoutFail<T>: Fail<T>()
    class FilterFail<T>: Fail<T>()

    abstract fun orElse(t: T): T
    abstract fun orElse(supplier: (DiscordResult<T>) -> T): T
    abstract fun orNull(): T?

    abstract fun ifPresent(func: (T) -> Unit): DiscordResult<T>
    abstract fun <R> map(func: (T) -> R): DiscordResult<R>
    abstract fun <R> flatMap(func: (T) -> DiscordResult<R>): DiscordResult<R>
    abstract fun filter(func: (T) -> Boolean): DiscordResult<T>

    abstract suspend fun ifPresentSuspend(func: suspend (T) -> Unit): DiscordResult<T>
    abstract suspend fun <R> mapSuspend(func: suspend (T) -> R): DiscordResult<R>
    abstract suspend fun <R> flatMapSuspend(func: suspend (T) -> DiscordResult<R>): DiscordResult<R>
    abstract suspend fun filterSuspend(func: suspend (T) -> Boolean): DiscordResult<T>
}