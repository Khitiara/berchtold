/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package berchtold

import kotlinx.io.charsets.Charset
import kotlinx.io.charsets.Charsets
import kotlinx.io.core.Input
import kotlinx.io.core.Output
import kotlinx.io.core.readTextExactCharacters
import kotlinx.io.core.writeText
import org.joml.Vector2f
import org.joml.Vector2i
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

interface Logging

inline fun <reified T : Logging> T.logger(): Logger = getLogger(T::class.java)

fun Input.readLengthEncodedText(charset: Charset = Charsets.UTF_8): String {
    val len = readInt()
    return readTextExactCharacters(len, charset)
}

fun Output.writeLengthEncodedText(string: String, charset: Charset = Charsets.UTF_8) {
    writeInt(string.length)
    writeText(string, charset = charset)
}

operator fun Vector2i.plus(other: Vector2i): Vector2i = this.add(other)
operator fun Vector2i.minus(other: Vector2i): Vector2i = this.sub(other)

operator fun Vector2f.plus(other: Vector2f): Vector2f = this.add(other)
operator fun Vector2f.minus(other: Vector2f): Vector2f = this.sub(other)