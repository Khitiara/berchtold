/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.Game
import berchtold.Identifier
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture

data class Sprite(val texture: Identifier, val left: Int, val right: Int, val top: Int, val bot: Int) {
    fun draw() {
        glActiveTexture(GL_TEXTURE0)
//        glColor4i(1, 1, 0, 0)
        glEnable(GL_TEXTURE_2D)
        Game.texmgr.bind(texture)
        glBegin(GL_QUADS)
        glTexCoord2f(0f, 1f)
        glVertex2i(left, top)
        glTexCoord2f(0f, 0f)
        glVertex2i(left, bot)
        glTexCoord2f(1f, 0f)
        glVertex2i(right, bot)
        glTexCoord2f(1f, 1f)
        glVertex2i(right, top)
        glEnd()
        glDisable(GL_TEXTURE_2D)
    }
}