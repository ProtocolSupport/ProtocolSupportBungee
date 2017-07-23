package protocolsupport.utils;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class ClassMap<T> {

	private final LinkedHashMap<Class<?>, T> map = new LinkedHashMap<>();

	public void register(Class<?> clazz, T t) {
		map.put(clazz, t);
	}

	public T get(Class<?> clazz) {
		T t = map.get(clazz);
		if (t != null) {
			return t;
		}
		for (Entry<Class<?>, T> entry : map.entrySet()) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				map.put(clazz, entry.getValue());
				return entry.getValue();
			}
		}
		return null;
	}

}
