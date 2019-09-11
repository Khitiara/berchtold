/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.resource.ResourceCollection
import kotlinx.io.core.Closeable
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glGenTextures

interface Texture : Closeable {
    val glId: Int

    fun load(mgr: ResourceCollection)

    fun bind() {
        RenderHelper.bind(glId)
    }

    val width: Int
    val height: Int
}

abstract class BaseTexture : Texture {
    final override val glId: Int = glGenTextures()

    override fun close() {
        glDeleteTextures(glId)
    }
}