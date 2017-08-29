package protocolsupport.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class Utils {

	public static class LazyNewInstance<T> {
		private final Class<? extends T> clazz;
		public LazyNewInstance(Class<? extends T> clazz) {
			this.clazz = clazz;
		}

		private T instance;
		public T getInstance() throws InstantiationException, IllegalAccessException {
			if (instance == null) {
				instance = clazz.newInstance();
			}
			return instance;
		}
	}

	public static String toStringAllFields(Object obj) {
		StringJoiner joiner = new StringJoiner(", ");
		Class<?> clazz = obj.getClass();
		do {
			try {
				for (Field field : clazz.getDeclaredFields()) {
					if (!Modifier.isStatic(field.getModifiers())) {
						ReflectionUtils.setAccessible(field);
						Object value = field.get(obj);
						if ((value == null) || !value.getClass().isArray()) {
							joiner.add(field.getName() + ": " + Objects.toString(value));
						} else {
							joiner.add(field.getName() + ": " + Arrays.deepToString(new Object[] {value}));
						}
					}
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Unable to get object fields values", e);
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		return obj.getClass().getName() + "(" + joiner.toString() + ")";
	}

	public static String clampString(String string, int limit) {
		return string.substring(0, string.length() > limit ? limit : string.length());
	}

	public static byte[] readBytes(ByteBuf from, int length) {
		byte[] data = new byte[length];
		from.readBytes(data, 0, length);
		return data;
	}

	public static <T> T getFromArrayOrNull(T[] array, int index) {
		if ((index >= 0) && (index < array.length)) {
			return array[index];
		} else {
			return null;
		}
	}

	public static String toLegacyText(String json) {
		if (json == null) {
			return "";
		}
		return Utils.toLegacyText(ComponentSerializer.parse(json));
	}

	public static String toLegacyText(BaseComponent[] components) {
		if (components == null) {
			return "";
		}
		final StringBuilder out = new StringBuilder();
		for (BaseComponent component : components) {
			out.append(component.toLegacyText());
		}
		return out.toString();
	}

	public static <T> T getJavaPropertyValue(String property, T defaultValue, Function<String, T> converter) {
		return getRawJavaPropertyValue("protocolsupport."+property, defaultValue, converter);
	}

	public static <T> T getRawJavaPropertyValue(String property, T defaultValue, Function<String, T> converter) {
		try {
			String value = System.getProperty(property);
			if (value != null) {
				return converter.apply(value);
			}
		} catch (Throwable t) {
		}
		return defaultValue;
	}

}
