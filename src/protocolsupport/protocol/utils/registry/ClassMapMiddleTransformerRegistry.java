package protocolsupport.protocol.utils.registry;

import java.util.NoSuchElementException;

import protocolsupport.utils.ClassMap;
import protocolsupport.utils.Utils.LazyNewInstance;

public class ClassMapMiddleTransformerRegistry<T, R> {

	private final ClassMap<LazyNewInstance<R>> registry = new ClassMap<>();
	private InitCallBack<R> callback;

	public void register(Class<? extends T> clazz, Class<? extends R> packetTransformer) {
		registry.register(clazz, new LazyNewInstance<>(packetTransformer));
	}

	public void setCallBack(InitCallBack<R> callback) {
		this.callback = callback;
	}

	public R getTransformer(Class<? extends T> clazz) throws InstantiationException, IllegalAccessException {
		LazyNewInstance<R> transformer = registry.get(clazz);
		if (transformer == null) {
			throw new NoSuchElementException("No transformer found for packet class " + clazz);
		}
		R object = transformer.getInstance();
		if (callback != null) {
			callback.onInit(object);
		}
		return object;
	}

	@FunctionalInterface
	public static interface InitCallBack<R> {
		public void onInit(R object);
	}

}
