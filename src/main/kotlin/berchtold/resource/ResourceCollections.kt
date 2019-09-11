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

class JarAssetsResourceCollection(jarPath: Path, assetsBase: String = "assets") :
    BaseResourceCollection(DefaultResourcePathnameScheme) {
    private val fs = FileSystems.newFileSystem(jarPath, javaClass.classLoader)
    private val basePath = fs.getPath(assetsBase)

    override fun resolve(path: String): Resource? = FileResource(basePath.resolve(path))
}

class ResourceDirResourceCollection(private val dirPath: Path) : BaseResourceCollection(
    DefaultResourcePathnameScheme
) {
    init {
        Files.createDirectories(dirPath)
    }

    override fun resolve(path: String): Resource? =
        dirPath.resolve(path).takeIf { Files.exists(it) }?.let { FileResource(it) }
}

class ClasspathResourceCollection(private val assetsBase: String = "assets") :
    BaseResourceCollection(DefaultResourcePathnameScheme) {
    override fun resolve(path: String): Resource? {
        println("Checking for classpath entry at $assetsBase/$path")
        val resource: URL? = javaClass.classLoader.getResource("$assetsBase/$path")
        return resource?.toURI()?.let { URIResource(it) }
    }
}

class CompoundResourceCollection(private val sources: List<ResourceCollection>) :
    ResourceCollection {
    override fun resolve(identifier: Identifier, ext: String): Resource? {
        for (source: ResourceCollection in sources) {
            val resource: Resource? = source.resolve(identifier, ext)
            if (resource != null) return resource
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