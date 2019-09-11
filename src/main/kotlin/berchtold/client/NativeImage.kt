/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold.client

import berchtold.resource.Resource
import kotlinx.io.core.Closeable
import kotlinx.io.errors.IOException
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import org.lwjgl.opengl.GL42.glTexStorage2D
import org.lwjgl.stb.STBImage.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer
import kotlin.properties.Delegates

class NativeImage private constructor() : Closeable {
    companion object {
        @JvmStatic
        fun read(r: Resource): NativeImage {
            val buf = Resource.readResource(r)
            buf.rewind()
            return read(buf).also { MemoryUtil.memFree(buf) }
        }

        @JvmStatic
        fun read(buf: ByteBuffer): NativeImage {
            require(MemoryUtil.memAddress(buf) != 0L) { "Invalid buffer" }
            MemoryStack.stackPush().use {
                val width: IntBuffer = it.mallocInt(1)
                val height: IntBuffer = it.mallocInt(1)
                val channels: IntBuffer = it.mallocInt(1)
                stbi_set_flip_vertically_on_load(true);
                val contentBuffer: ByteBuffer = stbi_load_from_memory(buf, width, height, channels, STBI_rgb_alpha)
                    ?: throw IOException("Could not load image: ${stbi_failure_reason()}")
                if (channels[0] != 4) throw IOException("Wrong number of channels in resulting image: ${channels[0]}")
                return NativeImage(width[0], height[0], true, contentBuffer)
            }
        }
    }

    private var pointer: ByteBuffer? = null
    private var isStbImage: Boolean by Delegates.notNull()
    var width: Int by Delegates.notNull()
        private set

    var height: Int by Delegates.notNull()
        private set

    var sizeBytes: Int by Delegates.notNull()
        private set

    constructor(width: Int, height: Int, calloc: Boolean) : this() {
        this.width = width
        this.height = height
        this.isStbImage = false
        this.sizeBytes = width * height * 4
        if (calloc) {
            this.pointer = MemoryUtil.memCalloc(width * height, 4)
        } else {
            this.pointer = MemoryUtil.memAlloc(sizeBytes)
        }
    }

    constructor(width: Int, height: Int, isStbImage: Boolean, pointer: ByteBuffer) : this() {
        this.width = width
        this.height = height
        this.isStbImage = isStbImage
        this.sizeBytes = width * height * 4
        this.pointer = pointer
    }

    override fun close() {
        if (pointer != null) when {
            isStbImage -> stbi_image_free(pointer)
            else -> MemoryUtil.memFree(pointer)
        }
        pointer = null
    }

    operator fun get(x: Int, y: Int): Int {
        require(x in 0 until width && y in 0 until height) { "($x, $y) outside of image bounds ($width, $height)" }
        require(pointer != null) { "Image is not allocated." }
        return pointer!!.getInt(x + y * width)
    }

    operator fun set(x: Int, y: Int, value: Int) {
        require(x in 0 until width && y in 0 until height) { "($x, $y) outside of image bounds ($width, $height)" }
        require(pointer != null) { "Image is not allocated." }
        pointer!!.putInt(x + y * width, value)
    }

    fun upload() {
        require(pointer != null) { "Image is not allocated." }
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)
        glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA8, width, height)
        glEnable(GL_TEXTURE_2D)
        glEnable(GL_LIGHTING)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
        glTexImage2D(GL_TEXTURE_2D, 0, 0, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pointer)
    }
}