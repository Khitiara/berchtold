/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold

import berchtold.client.TextureManager
import berchtold.game.ColonyGame
import berchtold.resource.ResourceCollection
import berchtold.resource.ResourceCollections
import org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE
import org.lwjgl.opengl.GL11.*

object Game : Logging {
    val resources: ResourceCollection = ResourceCollections.classpath("assets")
    //        ResourceCollections.compound(
//            ,
//            ResourceCollections.dir(Engine.assetsPath)
//        )
    val texmgr: TextureManager by lazy { TextureManager(resources) }

    fun init() {
        logger().info("Berchtold Starting...")
        Keyboard.PressEvent on {
            if (it.key == GLFW_KEY_ESCAPE)
                Engine.exit()
        }
    }

    fun renderInit() {
        texmgr.registerKnown()
        logger().info("Texture manager initialized...")
    }

    fun render() {
        glViewport(0, 0, Engine.width, Engine.height)
        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, 1600.0, 900.0, 0.0, 0.0, 500.0)
        ColonyGame.draw()
    }

    fun update() {
    }
}