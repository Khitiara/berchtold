/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.resource

import org.lwjgl.system.MemoryUtil
import java.io.FileInputStream
import java.io.InputStream
import java.net.URI
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path

sealed class Resource {
    companion object {
        fun readResource(ins: InputStream): ByteBuffer {
            if (ins is FileInputStream) {
                val chan: FileChannel = ins.channel
                val buf = MemoryUtil.memAlloc(chan.size().toInt() + 1)
                while (chan.read(buf) > 0) {
                    // NO-OP
                }
                return buf
            } else {
                var buf: ByteBuffer = MemoryUtil.memAlloc(8192)
                val chan = Channels.newChannel(ins)

                while (chan.read(buf) != -1) {
                    if (buf.remaining() == 0) {
                        buf = MemoryUtil.memRealloc(buf, buf.capacity() * 2)
                    }
                }
                return buf
            }
        }

        fun readResource(resource: Resource): ByteBuffer = resource.open().use { readResource(it) }
    }

    abstract fun open(): InputStream
}

data class FileResource(val path: Path) : Resource() {
    override fun open(): InputStream = Files.newInputStream(path)
}

data class URIResource(val uri: URI) : Resource() {
    override fun open(): InputStream = uri.toURL().openStream()
}