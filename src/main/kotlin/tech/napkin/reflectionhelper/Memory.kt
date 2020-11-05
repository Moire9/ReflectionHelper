/*
 * Copyright (C) 2020  SirNapkin1334
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <https://www.gnu.org/licenses/>.
 *
 * The author can be contacted via:
 *     Email: sirnapkin@protonmail.com
 *     Twitter: @SirNapkin1334
 *     Discord: @SirNapkin1334#7960
 *     Reddit: u/SirNapkin1334
 *     IRC: SirNapkin1334; Registered on Freenode, EFNet, possibly others
 *
 * If you wish to use this software in a way violating the terms, please
 * contact the author, as an exception can be made.
 */

package tech.napkin.reflectionhelper

import org.jetbrains.annotations.Range
import tech.napkin.reflectionhelper.Permission.tryOverridePermission

/**
 * A container class for the [memory] and [overhead] getters. This method uses a
 * few static variables and methods, so it needs its own utility class.
 *
 * @author SirNapkin1334
 */
@Suppress("unused")
object Memory {

	@JvmField val is64Bit: Boolean = System.getProperty("os.arch").contains("64")


	/**
	 * Whether or not to use [Permission.overridePermission].
	 *
	 * If true, using [memory] will produce illegal access warnings. If false, it will not,
	 * however it may not be able to access some fields and might be inaccurate.
	 */
	@JvmField var noWarnings: Boolean = true


	/**
	 * Return the number if 64-bit, otherwise halve it. This is to prevent lots of
	 * repetitive code when it comes to calculating overhead.
	 *
	 * @return the number of bits based off of the input and if the system is 64-bit or not
	 */
	@JvmStatic val Int.bittage: @Range(from = Int.MIN_VALUE.toLong(), to = Int.MAX_VALUE.toLong()) Long
		inline get() = if (is64Bit) toLong() else toLong() / 2


	/**
	 * The total amount of bits used in memory to store this object.
	 *
	 * The return value will *always* be divisible by 8, unless it is `null` on a
	 * 32-bit system, where it will be 4.
	 *
	 * Be aware - since this is recursive, calling it on a class with an extremely long
	 * chain of subclasses could produce a [StackOverflowError].
	 *
	 * If [noWarnings] is false then this will generate a reflection warning.
	 *
	 * @throws[StackOverflowError] if it is called on a class with a large quantity of nested fields
	 */
	@JvmStatic val Any?.memory: @Range(from = 4, to = Long.MAX_VALUE) Long get() = overhead + if (this == null) 0 else
		this::class.java.declaredFields.filter(Modifiers.Not::Static).sumOf {
			try {
				it.isAccessible = true
			} catch (e: Throwable) {
				if ((e is java.lang.reflect.InaccessibleObjectException || e is SecurityException) && noWarnings) {
					it.tryOverridePermission()
				} else throw e
			}
			/*
			 * Required because otherwise it will infinitely recurse once it finds a primitive,
			 * as it will box the primitive, and then try to get the size of the `value`
			 * primitive, and box that, and so on.
			 */
			when (this) {
				is Boolean, is Byte, is Char, is Short, is Int, is Float -> 32
				is Long, is Double -> 64
				else -> it.get(this).memory
			}
		}


	/**
	 * Since Strings are pretty common, I've made a String memory calculator to
	 * eliminate the need for costly reflective operations on it, since it's so
	 * simple.
	 *
	 * 96 = byte + int + boolean size
	 * 256/128 = byte[] overhead
	 */
	@JvmStatic val String.memory: Long get() = overhead + 96 + 256.bittage + 8 * length



	/**
	 * The memory overhead of an object (the amount of memory that is taken up by data
	 * that does not directly represent the state of the object).
	 */
	@JvmStatic val Any?.overhead: @Range(from = 0, to = 256) Long get() = (if (this == null) 8 else 192).bittage


	/*
	 * Store memory and overhead down here, as literals, so that if you call memory on
	 * one of these, it's routed straight to here, instead of having to go through
	 * costly reflective checks in the above code, at the cost of being quite verbose.
	 */

	@JvmStatic val Array<*>.memory: Long get() = overhead + sumOf { it.memory }


	/*
	 * The size of all primitives, except float and double, is 32, and they have no
	 * overhead.
	 */

	@JvmStatic val Boolean.memory  : @Range(from = 32, to = 32) Long inline get() = 32
	@JvmStatic val    Byte.memory  : @Range(from = 32, to = 32) Long inline get() = 32
	@JvmStatic val    Char.memory  : @Range(from = 32, to = 32) Long inline get() = 32
	@JvmStatic val   Short.memory  : @Range(from = 32, to = 32) Long inline get() = 32
	@JvmStatic val     Int.memory  : @Range(from = 32, to = 32) Long inline get() = 32
	@JvmStatic val   Float.memory  : @Range(from = 32, to = 32) Long inline get() = 32
	@JvmStatic val    Long.memory  : @Range(from = 64, to = 64) Long inline get() = 64
	@JvmStatic val  Double.memory  : @Range(from = 64, to = 64) Long inline get() = 64

	@JvmStatic val Boolean.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val    Byte.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val    Char.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val   Short.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val     Int.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val   Float.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val    Long.overhead: @Range(from =  0, to =  0) Long inline get() = 0
	@JvmStatic val  Double.overhead: @Range(from =  0, to =  0) Long inline get() = 0


	/*
	 * All arrays have an overhead of 256 on 64-bit. Their memory depends on the type
	 * that they store, and obviously, their size.
	 */

	@JvmStatic val BooleanArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 8  * size
	@JvmStatic val    CharArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 8  * size
	@JvmStatic val    ByteArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 8  * size
	@JvmStatic val   ShortArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 16 * size
	@JvmStatic val     IntArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 32 * size
	@JvmStatic val   FloatArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 32 * size
	@JvmStatic val    LongArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 64 * size
	@JvmStatic val  DoubleArray.memory: @Range(from = 128, to = Long.MAX_VALUE) Long inline get() = overhead + 64 * size

	@JvmStatic val BooleanArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val    CharArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val    ByteArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val   ShortArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val     IntArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val   FloatArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val    LongArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage
	@JvmStatic val  DoubleArray.overhead: @Range(from = 128, to = 256) Long inline get() = 256.bittage

}
