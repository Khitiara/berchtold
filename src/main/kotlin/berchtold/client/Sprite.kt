/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.Game
import berchtold.Identifier
import org.lwjgl.opengl.GL11.glTexCoord2f
import org.lwjgl.opengl.GL11.glVertex2i


data class Vertex(val x: Int, val y: Int, val u: Int, val v: Int) {
    fun draw(texWidth: Int, texHeight: Int) {
        glTexCoord2f(u.toFloat() / texWidth, v.toFloat() / texHeight)
        glVertex2i(x, y)
    }
}

data class Sprite(val texture: Identifier, val start: Vertex, val end: Vertex) {
    val topLeft = Vertex(
        kotlin.math.min(start.x, end.x), kotlin.math.max(start.x, start.y), if (start.x < end.x) start.u
        else end.u, if (start.y > end.y) start.v else end.v
    )
    val topRight = Vertex(
        kotlin.math.max(start.x, end.x), kotlin.math.max(start.x, start.y), if (start.x > end.x) start.u
        else end.u, if (start.y > end.y) start.v else end.v
    )
    val botLeft = Vertex(
        kotlin.math.min(start.x, end.x), kotlin.math.min(start.x, start.y), if (start.x < end.x) start.u
        else end.u, if (start.y < end.y) start.v else end.v
    )
    val botRight = Vertex(
        kotlin.math.max(start.x, end.x), kotlin.math.min(start.x, start.y), if (start.x > end.x) start.u
        else end.u, if (start.y < end.y) start.v else end.v
    )

    fun draw() {
        val (w, h) = Game.texmgr.bind(texture)
        topLeft.draw(w, h)
        topRight.draw(w, h)
        botRight.draw(w, h)
        botLeft.draw(w, h)
    }
}