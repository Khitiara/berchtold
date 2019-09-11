/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.game

import berchtold.Identifier
import berchtold.client.Sprite
import org.lwjgl.opengl.GL11.*

object ColonyGame {
    @JvmStatic
    val axeSprite: Sprite = Sprite(Identifier("berchtold", "icons/axe"), 32, 32 + 64, 32, 32 + 64)

    init {
        println(axeSprite)
    }

    @JvmStatic
    fun draw() {
        glPushMatrix()
        axeSprite.draw()
        glPopMatrix()
    }
}