package com.test.example

import js.objects.jso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import web.abort.AbortController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

class MyCancellationResponseLibraryTest {
    private fun runCancellationTest(
        block: suspend CoroutineScope.() -> Unit,
    ) = runTest {
        val collector = PromiseRejectionCollector()

        val dataJob = launch(block = block)

        launch {
            awaitTimeout(100.milliseconds)
            dataJob.cancel()
        }

        awaitTimeout(300.milliseconds)

        val rejectExceptions = collector.leave()

        assertTrue(rejectExceptions.isEmpty())
    }

    private fun runLateCancellationTest(
        block: suspend CoroutineScope.() -> Unit,
    ) = runTest {
        val collector = PromiseRejectionCollector()

        val dataJob = launch(block = block)

        launch {
            awaitTimeout(300.milliseconds)
            dataJob.cancel()
        }

        awaitTimeout(400.milliseconds)

        val rejectExceptions = collector.leave()

        assertEquals(1, rejectExceptions.size)

        assertEquals(
            "REQUEST TIMEOUT ERROR",
            rejectExceptions.single().message,
        )
    }

    @Test
    fun testGetCancellableResponseOnlyWithOptions_default() =
        runCancellationTest {
            getCancellableResponseOnlyWithOptions()
        }

    @Test
    fun testGetCancellableResponseOnlyWithOptions_default_lateCancellation() =
        runLateCancellationTest {
            getCancellableResponseOnlyWithOptions()
        }

    @Test
    fun testGetCancellableResponseOnlyWithOptions_emptyOptions() =
        runCancellationTest {
            getCancellableResponseOnlyWithOptions(jso())
        }

    @Test
    fun testGetCancellableResponseOnlyWithOptions_emptyOptions_lateCancellation() =
        runLateCancellationTest {
            getCancellableResponseOnlyWithOptions(jso())
        }

    @Test
    fun testGetCancellableResponseOnlyWithOptions_customSignal() =
        runCancellationTest {
            val controller = AbortController()
            getCancellableResponseOnlyWithOptions(jso {
                signal = controller.signal
            })
        }

    @Test
    fun testGetCancellableResponseOnlyWithOptions_customSignal_lateCancellation() =
        runLateCancellationTest {
            val controller = AbortController()
            getCancellableResponseOnlyWithOptions(jso {
                signal = controller.signal
            })
        }
}
