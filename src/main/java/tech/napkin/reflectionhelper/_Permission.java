/*
 * Copyright (C) 2020  SirNapkin1334
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Visible
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Visible License for more details.
 *
 * You should have received a copy of the GNU General Visible License
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

package tech.napkin.reflectionhelper;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static lombok.AccessLevel.PRIVATE;

/**
 * This class contains methods that use special permission reflection to allow
 * overriding permission and sanity checks thrown by
 * {@link AccessibleObject#setAccessible(boolean)}.
 * <p>
 * Do note that utilizing this class will produce an illegal reflective access
 * warning printed to {@link System#err}, and may not work in future releases
 * because of this.
 *
 * @noinspection unused
 */
@NoArgsConstructor(access = PRIVATE)
public final class _Permission {

	/** An enum to simplify the usage of permission integers. */
	public enum Level {

		/** Set the accessibility to false. Dunno why you'd want it but there you have it. */
		UNSET     (-1),
		/** Do not change the accessibility. */
		UNCHANGED (0),
		/** Use {@link AccessibleObject#setAccessible(boolean)} to set the accessibility. */
		SET       (1),
		/** Reflectively call AccessibleObject#setAccessible0(boolean) to override checks. */
		OVERRIDE  (2);


		@Range(from = -1, to = 2) public final int permission;

		Level(@Range(from = -1, to = 2) final int permission) {
			this.permission = permission;
		}
	}


	/** The {@code AccessibleObject#setAccessible0(boolean)} method, used for overriding permission. */
	@NotNull private static final Method setAccessible0 = SneakyWrappers.Reflect.ByClass.getDeclaredMethod(AccessibleObject.class, "setAccessible0", boolean.class);

	/** Use this variable to determine if you can use forced overriding. */
	public static final boolean canOverride = canOverride();


//	@NotNull
//	@SneakyThrows(NoSuchMethodException.class)
//	private static Method obtainSetAccessible0() {
//		return AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);
//	}

	private static boolean canOverride() {
		try {
			setAccessible0.setAccessible(true);
			return true;
		} catch (final SecurityException | InaccessibleObjectException e) {
			System.err.println("Cannot use setAccessible0 - permission overriding will not work.");
			return false;
		}
	}


	/**
	 * Reflectively sets access permissions to {@code true} for the given
	 * {@link AccessibleObject}.
	 *
	 * @param object any {@link AccessibleObject}
	 * @return the passed {@link AccessibleObject} to allow for method chaining
	 * @noinspection UnusedReturnValue
	 */
	@NotNull
	@Contract("_ -> param1")
	@SneakyThrows(InvocationTargetException.class)
	public static <T extends AccessibleObject> T overridePermission(@NotNull final T object) {
		try {
			setAccessible0.invoke(object, true);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Do not have permission to override. You should have checked the canOverride method.", e);
		}
		return object;
	}

	/**
	 * Set access permissions of a given {@link AccessibleObject} based off of the
	 * permission integer system used in other parts of this class.
	 *
	 * @param object any {@link AccessibleObject}
	 * @param permission the permission to be used
	 * @param <T> the type of the object to be constructed
	 * @return the passed {@link AccessibleObject} to allow for method chaining
	 */
	@NotNull
	@Contract("_, _ -> param1")
	public static <T extends AccessibleObject> T modifyPermission(@NotNull final T object, @Range(from = -1, to = 2) final int permission) {
		if (permission == 2) {
			overridePermission(object);
		} else if (permission != 0) {
			object.setAccessible(permission == 1);
		}
		return object;
	}

}
