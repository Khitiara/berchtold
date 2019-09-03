/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold

import berchtold.event.Event
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFWKeyCallback

object Keyboard : GLFWKeyCallback() {
    private val pressed: MutableSet<Int> = HashSet()

    data class PressEvent(val key: Int, val mods: Int) {
        companion object : Event<PressEvent>()

        fun emit() = Companion.emit(this)
    }

    data class ReleaseEvent(val key: Int, val mods: Int) {
        companion object : Event<ReleaseEvent>()

        fun emit() = Companion.emit(this)
    }

    operator fun contains(key: Int): Boolean = key in pressed

    override fun invoke(
        window: Long,
        key: Int,
        scancode: Int,
        action: Int,
        mods: Int
    ) {
        when (action) {
            GLFW_PRESS -> {
                PressEvent(key, mods).emit()
                pressed.add(key)
            }
            GLFW_RELEASE -> {
                ReleaseEvent(key, mods).emit()
                pressed.remove(key)
            }
        }
    }
}