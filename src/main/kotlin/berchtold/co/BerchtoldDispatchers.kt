/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.co

import berchtold.time.Timing
import kotlinx.coroutines.*
import kotlinx.coroutines.internal.MainDispatcherFactory
import java.util.*
import kotlin.coroutines.CoroutineContext

@UseExperimental(InternalCoroutinesApi::class, ExperimentalCoroutinesApi::class)
object BerchtoldDispatchers {

    val Client = GLDispatcher(Timing.clientTiming)
    val Update = GLDispatcher(Timing.updateTiming)

    class GLDispatcher(private val timing: Timing) : MainCoroutineDispatcher(), Delay {
        override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
            timing.startTimer(timeMillis / 1000.0) {
                with(continuation) { resumeUndispatched(Unit) }
            }
        }

        override val immediate: MainCoroutineDispatcher
            get() = this

        val queue: Queue<Runnable> = ArrayDeque()

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            queue.offer(block)
        }

        @ExperimentalCoroutinesApi
        override fun isDispatchNeeded(context: CoroutineContext): Boolean {
            return super.isDispatchNeeded(context)
        }

        fun process() {
            while (!queue.isEmpty()) {
                queue.poll()?.run()
            }
        }
    }
}

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object GLDispatcherFactory : MainDispatcherFactory {
    override fun createDispatcher(allFactories: List<MainDispatcherFactory>): MainCoroutineDispatcher =
        BerchtoldDispatchers.GLDispatcher(Timing.clientTiming)

    override val loadPriority: Int
        get() = 0
}