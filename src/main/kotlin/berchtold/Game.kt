/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold

import berchtold.client.TextureManager
import berchtold.resource.ResourceCollection
import berchtold.resource.ResourceCollections
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.opengl.GL11.*

object Game : Logging {
    val resources: ResourceCollection by lazy {
        ResourceCollections.compound(
            ResourceCollections.classpath("assets"),
            ResourceCollections.dir(Engine.assetsPath)
        )
    }
    val texmgr: TextureManager by lazy { TextureManager(resources) }

    fun init() {
        logger().info("Berchtold Starting...")
        Keyboard.PressEvent on {
            if (it.key == GLFW_KEY_ESCAPE)
                Engine.exit()
        }
        texmgr.registerKnown()
        logger().info("Texture manager initialized...")
    }

    fun render() {
        glColor3f(0f, 0f, 1f)
        glViewport(0, 0, Engine.width, Engine.height)
        glPushMatrix()
        glBegin(GL_QUADS)
        glVertex2f(-.25f, .25f)
        glVertex2f(-.5f, -.25f)
        glVertex2f(.5f, -.25f)
        glVertex2f(.25f, .25f)
        glPopMatrix()
        glEnd()
    }

    fun update() {
    }
}