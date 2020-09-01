package protocolsupport.utils;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getFieldValue(Object target, String name) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = target.getClass();
		do {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					return (T) setAccessible(field).get(target);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		return null;
	}

	public static void setFieldValue(Object target, String name, Object value) throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = target.getClass();
		do {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					setAccessible(field).set(target, value);
					return;
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
	}

	public static <T extends AccessibleObject> T setAccessible(T object) {
		object.setAccessible(true);
		return object;
	}

	public static void setStaticFinalField(Class<?> clazz, String fieldname, Object value) {
		try {
			Field field = setAccessible(clazz.getDeclaredField(fieldname));
			((MethodHandles.Lookup) setAccessible(MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP")).get(null))
			.findSetter(Field.class, "modifiers", int.class)
			.invokeExact(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
