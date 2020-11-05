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

@Suppress("unused")
object Primitives {

	@JvmField
	val boolean: Class<Boolean> = java.lang.Boolean.TYPE

	@JvmField
	val byte: Class<Byte> = java.lang.Byte.TYPE

	@JvmField
	val char: Class<Char> = Character.TYPE

	@JvmField
	val short: Class<Short> = java.lang.Short.TYPE

	@JvmField
	val int: Class<Int> = Integer.TYPE

	@JvmField
	val long: Class<Long> = java.lang.Long.TYPE

	@JvmField
	val float: Class<Float> = java.lang.Float.TYPE

	@JvmField
	val double: Class<Double> = java.lang.Double.TYPE

}
