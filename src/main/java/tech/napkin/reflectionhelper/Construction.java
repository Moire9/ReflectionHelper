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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

/**
 * A collection of methods to simplify the reflective construction of classes.
 * <p>
 * Thanks to the {@link _Permission} class, we can use special permission
 * reflection to allow overriding permission and sanity checks thrown by
 * {@link AccessibleObject#setAccessible(boolean)} and perform reflective
 * construction even on disallowed classes and methods (like {@link Class}).
 * <p>
 * Do note that doing this will produce an illegal reflective access warning
 * printed to {@link System#err}.
 *
 * @noinspection unused
 */
@NoArgsConstructor(access = PRIVATE)
public final class Construction {

	/**
	 * Construct an object from a provided constructor with the provided arguments.
	 * <p>
	 * Not in either subclass as it takes a constructor, not a class/name as argument.
	 *
	 * @param constructor a Constructor to be used for construction.
	 * @param permission an integer determining the permissions to be granted when
	 *                   constructing - 0: use
	 *                   {@link AccessibleObject#setAccessible(boolean)},
	 *                   > 0: reflectively set accessible boolean,
	 *                   < 0: do not change permissions.
	 * @param args any arguments to be passed to the constructor
	 * @param <T> the type of the object to be constructed
	 * @return a constructed object from the provided Constructor
	 * @throws IllegalAccessException if permission < 0 and access isn't allowed
	 * @throws InstantiationException if provided class is invalid or lacks constructor
	 * @throws InvocationTargetException if the constructor throws an exception
	 */
	@NotNull
	public static <T> T construct(@NotNull final Constructor<T> constructor, @Range(from = -1, to = 2) final int permission, @NotNull final Object... args) throws IllegalAccessException, InstantiationException, InvocationTargetException {
		return _Permission.modifyPermission(constructor, permission).newInstance(args);
	}

	/**
	 * Methods that act upon visible classes, taking {@link Class} parameters.
	 */
	@NoArgsConstructor(access = PRIVATE)
	public static final class Visible {

		/**
		 * Reflectively construct a new instance of the passed class with the provided
		 * arguments.
		 *
		 * @param clazz the class to initialize
		 * @param permission an integer determining the permissions to be granted when
		 *                   constructing - 0: use
		 *                   {@link AccessibleObject#setAccessible(boolean)},
		 *                   > 0: reflectively set accessible boolean,
		 *                   < 0: do not change permissions.
		 * @param args any arguments to be passed to the constructor
		 * @param <T> the type of the object to be constructed
		 * @return a constructed object of the specified type
		 * @throws NoSuchMethodException if no matching constructor is found
		 * @throws IllegalAccessException if permission < 0 and access isn't allowed
		 * @throws InstantiationException if provided class is invalid or lacks constructor
		 * @throws InvocationTargetException if the constructor throws an exception
		 */
		@NotNull
		public static <T> T construct(@NotNull final Class<T> clazz, @Range(from = -1, to = 2) final int permission, @NotNull final Object... args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
			return Construction.construct(clazz.getDeclaredConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new)), permission, args);
		}


		/**
		 * Get a constructor of the passed class with the specified argument types. This is
		 * useful when you want to construct a class, but must pass {@code null} to any of
		 * the constructor's arguments.
		 * <p>
		 * Unless you are making use of the permission overriding capabilities of this
		 * method, there is not much reason to use this method over
		 * {@link Class#getDeclaredConstructor(Class[])}
		 *
		 * @param clazz the class to initialize
		 * @param permission an integer determining the permissions to be granted when
		 *                   constructing - 0: use
		 *                   {@link AccessibleObject#setAccessible(boolean)},
		 *                   > 0: reflectively set accessible boolean,
		 *                   < 0: do not change permissions.
		 * @param classes the classes of the types to be passed to the constructor
		 * @param <T> the type of the object to be constructed
		 * @return a constructor for the object of the specified type
		 * @throws NoSuchMethodException if no matching constructor is found
		 */
		@NotNull
		public static <T> Constructor<T> classConstruct(@NotNull final Class<T> clazz, @Range(from = -1, to = 2) final int permission, @NotNull final Class<?>... classes) throws NoSuchMethodException {
			return _Permission.modifyPermission(clazz.getDeclaredConstructor(classes), permission);
		}

	}


	@NoArgsConstructor(access = PRIVATE)
	public static final class Invisible {

		/**
		 * Reflectively construct a new instance of the passed class with the provided
		 * arguments.
		 *
		 * @param clazz the class to initialize
		 * @param permission an integer determining the permissions to be granted when
		 *                   constructing - 0: use
		 *                   {@link AccessibleObject#setAccessible(boolean)},
		 *                   > 0: reflectively set accessible boolean,
		 *                   < 0: do not change permissions.
		 * @param args any arguments to be passed to the constructor
		 * @return a constructed object of the specified type
		 * @throws NoSuchMethodException if no matching constructor is found
		 * @throws IllegalAccessException if permission < 0 and access isn't allowed
		 * @throws InstantiationException if provided class is invalid or lacks constructor
		 * @throws InvocationTargetException if the constructor throws an exception
		 */
		public static Object construct(@NotNull final String clazz, final @Range(from = -1, to = 2) int permission, @NotNull final Object... args) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
			return Construction.construct(Class.forName(clazz).getDeclaredConstructor(Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new)), permission, args);
		}


		/**
		 * Get a constructor of the passed class with the specified argument types. This is
		 * useful when you want to construct a class, but must pass {@code null} to any of
		 * the constructor's arguments.
		 * <p>
		 * Unless you are making use of the permission overriding capabilities of this
		 * method, there is not much reason to use this method over
		 * {@link Class#getDeclaredConstructor(Class[])}
		 *
		 * @param clazz the class to initialize
		 * @param permission an integer determining the permissions to be granted when
		 *                   constructing - 0: use
		 *                   {@link AccessibleObject#setAccessible(boolean)},
		 *                   > 0: reflectively set accessible boolean,
		 *                   < 0: do not change permissions.
		 * @param classes the classes of the types to be passed to the constructor
		 * @return a constructor for the object of the specified type
		 * @throws NoSuchMethodException if no matching constructor is found
		 */
		@NotNull
		public static Constructor<?> classConstruct(@NotNull final String clazz, @Range(from = -1, to = 2) final int permission, @NotNull final Class<?>... classes) throws NoSuchMethodException, ClassNotFoundException {
			return _Permission.modifyPermission(Class.forName(clazz).getDeclaredConstructor(classes), permission);
		}

	}

}

