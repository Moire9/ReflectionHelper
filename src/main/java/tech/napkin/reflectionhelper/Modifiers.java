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

package tech.napkin.reflectionhelper;

import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

import static lombok.AccessLevel.PRIVATE;


/**
 * Wrapper classes for {@code Modifier.is<property>(Field#getModifiers())}.
 * Useful for streaming, as they are all {@link Predicate}s.
 */
@NoArgsConstructor(access = PRIVATE)
@SuppressWarnings("unused")
public final class Modifiers {

	@NoArgsConstructor(access = PRIVATE)
	public static final class Is {

		public static boolean Public(final Field f) {
			return Modifier.isPublic(f.getModifiers());
		}

		public static boolean Private(final Field f) {
			return Modifier.isPrivate(f.getModifiers());
		}

		public static boolean Protected(final Field f) {
			return Modifier.isProtected(f.getModifiers());
		}

		public static boolean Final(final Field f) {
			return Modifier.isFinal(f.getModifiers());
		}

		public static boolean Synchronized(final Field f) {
			return Modifier.isSynchronized(f.getModifiers());
		}

		public static boolean Volatile(final Field f) {
			return Modifier.isVolatile(f.getModifiers());
		}

		public static boolean Transient(final Field f) {
			return Modifier.isTransient(f.getModifiers());
		}

		public static boolean Native(final Field f) {
			return Modifier.isNative(f.getModifiers());
		}

		public static boolean Interface(final Field f) {
			return Modifier.isInterface(f.getModifiers());
		}

		public static boolean Abstract(final Field f) {
			return Modifier.isAbstract(f.getModifiers());
		}

		public static boolean Strict(final Field f) {
			return Modifier.isStrict(f.getModifiers());
		}

		public static boolean Static(final Field f) {
			return Modifier.isStatic(f.getModifiers());
		}

		public static boolean PackagePrivate(final Field f) {
			return !Not.PackagePrivate(f);
		}

	}


	@NoArgsConstructor(access = PRIVATE)
	public static final class Not {

		public static boolean Public(final Field f) {
			return !Is.Public(f);
		}

		public static boolean Private(final Field f) {
			return !Is.Private(f);
		}

		public static boolean Protected(final Field f) {
			return !Is.Protected(f);
		}

		public static boolean Final(final Field f) {
			return !Is.Final(f);
		}

		public static boolean Synchronized(final Field f) {
			return !Is.Synchronized(f);
		}

		public static boolean Volatile(final Field f) {
			return !Is.Volatile(f);
		}

		public static boolean Transient(final Field f) {
			return !Is.Transient(f);
		}

		public static boolean Native(final Field f) {
			return !Is.Native(f);
		}

		public static boolean Interface(final Field f) {
			return !Is.Interface(f);
		}

		public static boolean Abstract(final Field f) {
			return !Is.Abstract(f);
		}

		public static boolean Strict(final Field f) {
			return !Is.Strict(f);
		}

		public static boolean Static(final Field f) {
			return !Is.Static(f);
		}

		public static boolean PackagePrivate(final Field f) {
			return Is.Public(f) || Is.Private(f) || Is.Protected(f);
		}

	}

}
