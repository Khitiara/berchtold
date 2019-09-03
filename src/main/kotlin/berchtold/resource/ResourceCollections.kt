/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.resource

import berchtold.Identifier
import java.net.URL
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class JarAssetsResourceCollection(jarPath: Path, assetsBase: String = "assets") :
    BaseResourceCollection(DefaultResourcePathnameScheme) {
    private val fs = FileSystems.newFileSystem(jarPath, javaClass.classLoader)
    private val basePath = fs.getPath(assetsBase)

    override fun resolve(path: String): Path? = basePath.resolve(path)
}

class ResourceDirResourceCollection(private val dirPath: Path) : BaseResourceCollection(
    DefaultResourcePathnameScheme
) {
    init {
        Files.createDirectories(dirPath)
    }

    override fun resolve(path: String): Path? = dirPath.resolve(path)
}

class ClasspathResourceCollection(private val assetsBase: String = "assets") :
    BaseResourceCollection(DefaultResourcePathnameScheme) {
    override fun resolve(path: String): Path? {
        val resource: URL? = javaClass.getResource("${assetsBase}/$path")
        return if (resource != null) Paths.get(resource.toURI()) else null
    }
}

class CompoundResourceCollection(private val sources: List<ResourceCollection>) :
    ResourceCollection {
    override fun resolve(identifier: Identifier): Path? {
        for (source: ResourceCollection in sources) {
            val path = source.resolve(identifier)
            if (path != null) return path
        }
        return null
    }
}

object ResourceCollections {
    fun jar(tgt: Path, base: String = "assets"): ResourceCollection =
        JarAssetsResourceCollection(tgt, base)

    fun dir(base: Path): ResourceCollection = ResourceDirResourceCollection(base)
    fun classpath(base: String = "assets"): ResourceCollection =
        ClasspathResourceCollection(base)

    fun compound(vararg sources: ResourceCollection): ResourceCollection =
        CompoundResourceCollection(sources.toList())
}