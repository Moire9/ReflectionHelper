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

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A class that allows for the disabling of illegal access warnings.
 *
 * @noinspection unused
 */
public final class ReflectionWarnings extends PrintStream {

	@NotNull private static final ReflectionWarnings ourStream = new ReflectionWarnings();

	/** The old error stream, for restoring. */
	private static PrintStream oldStream;

	@NotNull private static final Object theLogger = SneakyWrappers.Objects.Get.ByName.field("jdk.internal.module.IllegalAccessLogger", "logger", 1, null);
	@NotNull private static final Field theStream = SneakyWrappers.Reflect.ByName.getDeclaredField("jdk.internal.module.IllegalAccessLogger", 2, "warningStream");

	@NotNull private static final Method getCallerClass = SneakyWrappers.Reflect.ByName.getDeclaredMethod("jdk.internal.reflect.Reflection", 2, "getCallerClass");


	private static Field obtainStream() {
		return SneakyWrappers.Reflect.ByName.getDeclaredField("jdk.internal.module.IllegalAccessLogger", 2, "warningStream");
	}

//	@SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
//	private static IllegalAccessLogger obtainLogger() {
//		return (IllegalAccessLogger) _Permission.modifyPermission(IllegalAccessLogger.class.getDeclaredField("logger"), 0).get(null);
//	}
//
//	@SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
//	private static OutputStream obtainSysOut() {
//		return (OutputStream) _Permission.modifyPermission(FilterOutputStream.class.getDeclaredField("out"), 0).get(theStream.get(theLogger));
//	}

	@SneakyThrows(IllegalAccessException.class)
	public static void disableWarnings() {
		if (oldStream == null) {
			oldStream = (PrintStream) theStream.get(theLogger);
			theStream.set(theLogger, ourStream);
		} else {
			throw new IllegalStateException("Warnings already disabled!");
		}
	}

	@SneakyThrows(IllegalAccessException.class)
	public static void enableWarnings() {
		if (oldStream != null) {
			theStream.set(theLogger, oldStream);
		} else {
			throw new IllegalStateException("Warnings already enabled!");
		}
	}

	private ReflectionWarnings() {
		super((OutputStream) SneakyWrappers.Objects.Get.ByClass.field(FilterOutputStream.class, "out", 0,
			SneakyWrappers.Objects.Get.ByField.field(theStream, theLogger)));
	}

	@SneakyThrows({IllegalAccessException.class, InvocationTargetException.class})
	@Override
	public void println(final @Nullable String x) {
		if (!(SneakyWrappers.Reflect.getClass("jdk.internal.module.IllegalAccessLogger").equals(getCallerClass.invoke(null)) &&
			String.valueOf(x).startsWith("WARNING: "))) {
			super.println(x);
		}
	}


}
