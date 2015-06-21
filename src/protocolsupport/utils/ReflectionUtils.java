package protocolsupport.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Object target, String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = target.getClass();
		do {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(name)) {
					return (T) setAccessible(method).invoke(target);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		return null;
	}

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
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
	}

	public static <T extends AccessibleObject> T setAccessible(T object) {
		object.setAccessible(true);
		return object;
	}

	public static void setStaticFinalField(Field field, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setAccessible(Field.class.getDeclaredField("modifiers")).setInt(field, field.getModifiers() & ~Modifier.FINAL);
		setAccessible(Field.class.getDeclaredField("root")).set(field, null);
		setAccessible(Field.class.getDeclaredField("overrideFieldAccessor")).set(field, null);
		setAccessible(field).set(null, newValue);
	}

}
