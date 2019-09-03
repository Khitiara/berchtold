/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.resource

import berchtold.Identifier
import java.nio.file.Path

interface ResourceCollection {
    fun resolve(identifier: Identifier): Path?
}

abstract class BaseResourceCollection(private val scheme: ResourcePathnameScheme) :
    ResourceCollection {
    override fun resolve(identifier: Identifier): Path? = resolve(scheme.toPath(identifier))

    protected abstract fun resolve(path: String): Path?
}

interface ResourcePathnameScheme {
    fun toPath(identifier: Identifier): String
}

object DefaultResourcePathnameScheme : ResourcePathnameScheme {
    override fun toPath(identifier: Identifier): String = "${identifier.domain}/${identifier.path}"
}

