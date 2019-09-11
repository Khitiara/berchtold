/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.resource

import berchtold.Identifier
import java.nio.file.Path

interface ResourceCollection {
    fun resolve(identifier: Identifier, ext: String): Resource?
}

abstract class BaseResourceCollection(private val scheme: ResourcePathnameScheme) :
    ResourceCollection {
    override fun resolve(identifier: Identifier, ext: String): Resource? = resolve(scheme.toPath(identifier, ext))

    protected abstract fun resolve(path: String): Resource?
}

interface ResourcePathnameScheme {
    fun toPath(identifier: Identifier, ext: String): String
}

object DefaultResourcePathnameScheme : ResourcePathnameScheme {
    override fun toPath(identifier: Identifier, ext: String): String = "${identifier.domain}/${identifier.path}.$ext"
}

