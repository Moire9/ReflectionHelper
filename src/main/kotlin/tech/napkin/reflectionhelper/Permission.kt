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
import tech.napkin.reflectionhelper.Primitives.boolean
import java.lang.reflect.AccessibleObject
import java.lang.reflect.InaccessibleObjectException
import java.lang.reflect.Method

import kotlin.reflect.KClass


//inline fun <T> betterTry(execute: () -> T?, vararg catch: Pair<Array<KClass<out Throwable>>, (Throwable) -> T?> = arrayOf(Pair(arrayOf(Throwable::class), {throw it})), finally: () -> T? = {null}): T? {
//	return try {
//		execute.invoke()
//	} catch (e: Throwable) {
//		for (x in catch) {
//			for (y in x.first) {
//				if (e::class == y) {
//					x.second.invoke(e)
//				}
//			}
//		}
//		throw e
//	} finally {
//		finally.invoke()
//	}
//}

object Permission {

	/** An enum to simplify the usage of permission integers. */
	@Suppress("unused", "UNUSED_PARAMETER")
	enum class Level(permission: @Range(from = -1, to = 2) Int) {

		/** Set the accessibility to false. Dunno why you'd want it but there you have it. */
		UNSET     (-1),
		/** Do not change the accessibility. */
		UNCHANGED (0),
		/** Use [AccessibleObject.setAccessible] to set the accessibility. */
		SET       (1),
		/** Reflectively call [AccessibleObject.setAccessible0] to override checks. */
		OVERRIDE  (2);

	}


	/**
	 * Lazy to prevent the generation of the reflection warning on clinit. This allows
	 * usage like `field.modifyPermission(1)` which shouldn't generate a warning but
	 * does anyways, since executing that calls clinit which would, if not `lazy`, set
	 * this variable's value.
	 */
	private val setAccessible0: Method by lazy {
		AccessibleObject::class.java.getDeclaredMethod("setAccessible0", boolean::class.java)
	}

	@Suppress("MemberVisibilityCanBePrivate")
	val canOverride: Boolean by lazy {
		return@lazy try {
			setAccessible0.isAccessible = true
			true
		} catch (e: Throwable) {
			when(e) {
				is SecurityException, is InaccessibleObjectException -> {
					System.err.println("Cannot use setAccessible0 - permission overriding will not work.")
					false
				}
				else -> throw e
			}
		}
	}


	@JvmStatic
	fun <T : AccessibleObject> T.overridePermission(): T = apply {
		if (canOverride) {
			setAccessible0.invoke(this, true)
		} else {
			throw IllegalAccessException("Do not have permission to override.")
		}
	}

	@JvmStatic
	fun <T: AccessibleObject> T.tryOverridePermission(): T = this.apply {
		return modifyPermission(if (canOverride) 2 else 1)
	}

	@JvmStatic
	fun <T : AccessibleObject> T.modifyPermission(permission: @Range(from = -1, to = 2) Int = 1): T = this.apply {
		if (permission == 2) {
			overridePermission()
		} else if (permission != 0) {
			isAccessible = permission == 1
		}
	}

}
