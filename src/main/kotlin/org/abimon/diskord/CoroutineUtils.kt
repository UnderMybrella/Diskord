package org.abimon.diskord

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull

public suspend inline fun <T> awaitResultWithTimeoutOrNull(timeMillis: Long, crossinline block: (CancellableContinuation<T>) -> Unit): T? =
        withTimeoutOrNull(timeMillis) { suspendCancellableCoroutine(block) }

public suspend inline fun <T> awaitDiscordResult(timeMillis: Long, crossinline block: (CancellableContinuation<DiscordResult<T>>) -> Unit): DiscordResult<T> =
        withTimeoutOrNull(timeMillis) { suspendCancellableCoroutine(block) } ?: DiscordResult.TimeoutFail()