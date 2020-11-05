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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static lombok.AccessLevel.PRIVATE;

/**
 * Class for wrapping reflective operations and capturing their checked exceptions
 * with {@link lombok.SneakyThrows}. You should only use this for operations that
 * you are 100% sure will not work, otherwise bad things will probably happen.
 * <p>
 * Package-private to prevent people using this class in their code since it is
 * most certainly a bad idea, if you really want to use it, reflect or something,
 * you got a lot of methods to help you with that here.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = PRIVATE)
public final class SneakyWrappers {

	@NoArgsConstructor(access = PRIVATE)
	public static final class Objects {

		@NoArgsConstructor(access = PRIVATE)
		public static final class Get {

			@NoArgsConstructor(access = PRIVATE)
			public static final class ByClass {

				@SuppressWarnings("unchecked")
				@SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
				public static <T> T field(@NotNull final Class<?> clazz, @NotNull final String fieldName, @Range(from = -1, to = 2) final int permission, @Nullable final Object instance) {
					return (T) _Permission.modifyPermission(clazz.getDeclaredField(fieldName), permission).get(instance);
				}

				public static <T> T field(@NotNull final Class<?> clazz, @NotNull final String fieldName, @Nullable final Object instance) {
					return field(clazz, fieldName, 0, instance);
				}

			}


			@NoArgsConstructor(access = PRIVATE)
			public static final class ByName {

				@SuppressWarnings("unchecked")
				@SneakyThrows({ClassNotFoundException.class, NoSuchFieldException.class, IllegalAccessException.class})
				public static <T> T field(@NotNull final String className, @NotNull final String fieldName, @Range(from = -1, to = 2) final int permission, @Nullable final Object instance) {
					return (T) _Permission.modifyPermission(Class.forName(className).getDeclaredField(fieldName), permission).get(instance);
				}

				public static <T> T field(@NotNull final String className, @NotNull final String fieldName, @Nullable final Object instance) {
					return field(className, fieldName, 0, instance);
				}

			}


			@NoArgsConstructor(access = PRIVATE)
			public static final class ByField {

				@SuppressWarnings("unchecked")
				@SneakyThrows(IllegalAccessException.class)
				public static <T> T field(@NotNull final Field field, @Range(from = -1, to = 2) final int permission, @Nullable final Object instance) {
					return (T) _Permission.modifyPermission(field, permission).get(instance);
				}

				public static <T> T field(@NotNull final Field field, @Nullable final Object instance){
					return field(field, 0, instance);
				}

			}


		}


		@NoArgsConstructor(access = PRIVATE)
		public static final class Set {

			@NoArgsConstructor(access = PRIVATE)
			public static final class ByClass {

				@SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
				public static void field(@NotNull final Class<?> clazz, @NotNull final String fieldName, @Range(from = -1, to = 2) final int permission, @Nullable final Object instance, @Nullable final Object value) {
					_Permission.modifyPermission(clazz.getDeclaredField(fieldName), permission).set(instance, value);
				}

				public static void field(@NotNull final Class<?> clazz, @NotNull final String fieldName, @Nullable final Object instance, @Nullable final Object value) {
					field(clazz, fieldName, 0, instance, value);
				}

			}

			@NoArgsConstructor(access = PRIVATE)
			public static final class ByName {

				@SneakyThrows({NoSuchFieldException.class, ClassNotFoundException.class, IllegalAccessException.class})
				public static void field(@NotNull final String className, @NotNull final String fieldName, @Range(from = -1, to = 2) final int permission, @Nullable final Object instance, @Nullable final Object value) {
					_Permission.modifyPermission(Class.forName(className).getDeclaredField(fieldName), permission).set(instance, value);
				}

				public static void field(@NotNull final String className, @NotNull final String fieldName, @Nullable final Object instance, @Nullable final Object value) {
					field(className, fieldName, 0, instance, value);
				}

			}

			@NoArgsConstructor(access = PRIVATE)
			public static final class ByField {

				@SneakyThrows(IllegalAccessException.class)
				public static void field(@NotNull final Field field, @Range(from = -1, to = 2) final int permission, @Nullable final Object instance, @Nullable final Object value) {
					_Permission.modifyPermission(field, permission).set(instance, value);
				}

				public static void field(@NotNull final Field field, @Nullable final Object instance, @Nullable final Object value) {
					field(field, 0, instance, value);
				}

			}

		}

	}


	@NoArgsConstructor(access = PRIVATE)
	public static final class Reflect {

		@SneakyThrows(ClassNotFoundException.class)
		public static Class<?> getClass(@NotNull final String name) {
			return Class.forName(name);
		}


		@NoArgsConstructor(access = PRIVATE)
		public static final class ByName {

			@SneakyThrows(NoSuchMethodException.class)
			public static Method getDeclaredMethod(@NotNull final String className, @Range(from = -1, to = 2) final int permission, @NotNull final String name, @NotNull final Class<?>... parameterTypes) {
				return _Permission.modifyPermission(Reflect.getClass(className).getDeclaredMethod(name, parameterTypes), permission);
			}

			public static Method getDeclaredMethod(@NotNull final String className, @NotNull final String name, @NotNull final Class<?>... parameterTypes) {
				return getDeclaredMethod(className, 0, name, parameterTypes);
			}

			@SneakyThrows(NoSuchFieldException.class)
			public static Field getDeclaredField(@NotNull final String className, @Range(from = -1, to = 2) final int permission, @NotNull final String name) {
				return _Permission.modifyPermission(Reflect.getClass(className).getDeclaredField(name), permission);
			}

			public static Field getDeclaredField(@NotNull final String className, @NotNull final String name) {
				return getDeclaredField(className, 0, name);
			}

			@SneakyThrows(NoSuchMethodException.class)
			public static Constructor<?> getDeclaredConstructor(@NotNull final String className, @Range(from = -1, to = 2) final int permission, @NotNull final Class<?>... parameterTypes) {
				return _Permission.modifyPermission(Reflect.getClass(className).getDeclaredConstructor(parameterTypes), permission);
			}

			public static Constructor<?> getDeclaredConstructor(@NotNull final String className, @NotNull final Class<?>... parameterTypes) {
				return getDeclaredConstructor(className, 0, parameterTypes);
			}

		}

		@NoArgsConstructor(access = PRIVATE)
		public static final class ByClass {

			@SneakyThrows(NoSuchMethodException.class)
			public static Method getDeclaredMethod(@NotNull final Class<?> clazz, @NotNull final String name, @NotNull final Class<?>... parameterTypes) {
				return clazz.getDeclaredMethod(name, parameterTypes);
			}

			@SneakyThrows(NoSuchFieldException.class)
			public static Field getDeclaredField(@NotNull final Class<?> clazz, @NotNull final String name) {
				return clazz.getDeclaredField(name);
			}

			@SneakyThrows(NoSuchMethodException.class)
			public static Constructor<?> getDeclaredConstructor(@NotNull final Class<?> clazz, @NotNull final Class<?>... parameterTypes) {
				return clazz.getDeclaredConstructor(parameterTypes);
			}

		}




	}

}
