/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glBindTexture

object RenderHelper {
    private var boundTexture: Int = -1

    fun bind(id: Int) {
        if (boundTexture != id) {
            boundTexture = id
            glBindTexture(GL_TEXTURE_2D, id)
        }
    }
}