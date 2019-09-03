/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.resource.ResourceCollection
import org.lwjgl.opengl.GL11.glGenTextures

interface Texture {
    val glId: Int

    fun load(mgr: ResourceCollection)

    fun bind() {
        RenderHelper.bind(glId)
    }

    val width: Int
    val height: Int
}

abstract class BaseTexture : Texture {
    override val glId: Int by lazy { glGenTextures() }
}