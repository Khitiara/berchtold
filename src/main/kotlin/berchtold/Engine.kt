/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold

import berchtold.co.BerchtoldDispatchers
import berchtold.time.Timing
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWFramebufferSizeCallback
import org.lwjgl.glfw.GLFWKeyCallback
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.properties.Delegates.notNull


object Engine : Logging, CliktCommand() {
    private var errorCallback: GLFWErrorCallback? = null
    private var keyCallback: GLFWKeyCallback? = null

    private val startWidth: Int by option("--width", "-w", help = "Width").int().default(1600)
    private val startHeight: Int by option("--height", "-h", help = "Height").int().default(900)

    val assetsPath: Path by option(
        "--assets",
        "--assetsDir",
        help = "Assets Directory"
    ).path().default(Paths.get("./assets"))

    var width: Int by notNull()
    var height: Int by notNull()

    private var window: Long = NULL

    override fun run() {
        try {
            init()
            thread(name = "Render Thread") {
                logger().info("Render Thread Starting...")
                renderLoop()
            }
            thread(name = "Update Thread") {
                logger().info("Update Thread Starting...")
                updateLoop()
            }
            while (!glfwWindowShouldClose(window))
                glfwWaitEvents()
            glfwFreeCallbacks(window)
            glfwDestroyWindow(window)
            keyCallback?.free()
        } catch (ex: Throwable) {
            logger().error("Uncaught exception", ex)
        } finally {
            glfwTerminate()
            errorCallback?.free()
        }
    }

    private fun renderLoop() {
        glfwMakeContextCurrent(window)
        GL.createCapabilities()
        glClearColor(0F, 0F, 0F, 0F)

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            Timing.clientTiming.updateDelta()
            BerchtoldDispatchers.Client.process()

            Game.render()
            Timing.clientTiming.updateFps()

            Timing.clientTiming.update()

            glfwSwapBuffers(window)
        }
    }

    private fun updateLoop() {
        while (!glfwWindowShouldClose(window)) {
            Timing.updateTiming.updateDelta()
            BerchtoldDispatchers.Update.process()
            Game.update()

            Timing.updateTiming.updateFps()
            Timing.updateTiming.update()
        }
    }

    private fun init() {
        width = startWidth
        height = startHeight
        errorCallback = glfwSetErrorCallback(GLFWErrorCallback.createThrow())

        check(glfwInit()) { "GLFW failed to start" }
        logger().debug("GLFW Initialized")
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = glfwCreateWindow(width, height, "Berchtold", NULL, NULL)
        if (window == NULL)
            throw RuntimeException("Failed to create window")
        logger().debug("Window created")

        keyCallback = glfwSetKeyCallback(window, Keyboard)

        glfwSetFramebufferSizeCallback(window, object : GLFWFramebufferSizeCallback() {
            override fun invoke(window: Long, w: Int, h: Int) {
                if (w > 0 && h > 0) {
                    width = w
                    height = h
                }
            }
        })
        glfwSetWindowAspectRatio(window, 16, 9)

        val vidmode: GLFWVidMode? = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(window, (vidmode!!.width() - width) / 2, (vidmode.height() - height) / 2)
        glfwShowWindow(window)

        Timing.clientTiming.init()
        Timing.updateTiming.init()
        Game.init()
    }

    fun exit() = glfwSetWindowShouldClose(window, true)
}
