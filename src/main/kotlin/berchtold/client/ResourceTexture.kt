/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.Identifier
import berchtold.Logging
import berchtold.resource.ResourceCollection
import kotlinx.io.errors.IOException
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glEnable
import kotlin.properties.Delegates.notNull

class ResourceTexture(val location: Identifier) : BaseTexture(), Logging {
    override fun load(mgr: ResourceCollection) {
        NativeImage.read(mgr.resolve(location, "png") ?: throw IOException("Resource not found: $location")).use {
            glEnable(GL_TEXTURE_2D)
            bind()
            it.upload()
            this.width = it.width
            this.height = it.height
            RenderHelper.bind(0)
        }
    }

    override var width: Int by notNull()
        private set
    override var height: Int by notNull()
        private set
}