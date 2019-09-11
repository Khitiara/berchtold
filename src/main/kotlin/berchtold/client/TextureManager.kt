/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.Identifier
import berchtold.Logging
import berchtold.resource.ResourceCollection
import java.io.IOException

class TextureManager(private val resources: ResourceCollection) : Logging {
    private val created: MutableMap<Identifier, Texture> = HashMap()
    private val starting: Set<Identifier> = hashSetOf(Identifier("berchtold", "icons/axe"))

    fun bind(texture: Identifier): Pair<Int, Int> {
        val tex = created.getOrPut(texture) { registerTexture(texture) }
        tex.bind()
        return Pair(tex.width, tex.height)
    }

    fun registerTexture(id: Identifier): Texture = try {
        ResourceTexture(id).also { it.load(resources) }
    } catch (e: IOException) {
        throw e // TODO
    }

    fun registerKnown() {
        starting.forEach { created.put(it, registerTexture(it)) }
    }

}
