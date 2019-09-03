/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.time

import kotlin.properties.Delegates.notNull

data class Timer(val length: Double, private val cb: () -> Unit) {
    var start: Double by notNull()
        private set
    private val end: Double get() = start + length

    fun start(timing: Timing) {
        timing.timers.add(this)
        start = timing.time
    }

    fun update(timing: Timing): Boolean {
        val done = timing.time >= end
        if (done) cb()
        return done
    }
}