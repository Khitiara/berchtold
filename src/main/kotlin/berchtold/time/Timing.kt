/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.time

import org.lwjgl.glfw.GLFW.glfwGetTime

class Timing {

    companion object {
        val clientTiming = Timing()
        val updateTiming = Timing()
    }

    var lastLoopTime: Double = 0.0
        private set
    private var timeCount: Double = 0.0
    private var _fps: Int = 0
    private var fpsCounter: Int = 0

    var timers: MutableSet<Timer> = HashSet()
        private set

    val fps: Int
        get() = if (_fps > 0) _fps else fpsCounter

    val time: Double
        get() = glfwGetTime()

    var delta: Double = 0.0
        private set

    fun updateDelta(): Double {
        val t = time
        val d = t - lastLoopTime
        lastLoopTime = t
        timeCount += d
        delta = d
        return d
    }

    fun updateFps() {
        fpsCounter++
    }

    fun update() {
        if (timeCount > 1) {
            _fps = fpsCounter
            fpsCounter = 0
            timeCount -= 1
        }
        timers = HashSet(timers.filter { it.update(this) })
    }

    fun startTimer(length: Double, cb: () -> Unit) = Timer(length, cb).start(this)

    fun init() {
        lastLoopTime = time
    }
}