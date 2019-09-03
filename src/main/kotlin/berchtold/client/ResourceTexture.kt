/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.Identifier
import berchtold.resource.ResourceCollection
import kotlin.properties.Delegates.notNull

class ResourceTexture(location: Identifier) : BaseTexture() {
    override fun load(mgr: ResourceCollection) {
        TODO("not implemented")
    }

    override var width: Int by notNull()
    override var height: Int by notNull()
}