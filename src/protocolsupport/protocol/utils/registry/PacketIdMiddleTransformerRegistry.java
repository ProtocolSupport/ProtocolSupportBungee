package protocolsupport.protocol.utils.registry;

import java.util.NoSuchElementException;

import net.md_5.bungee.protocol.Protocol;
import protocolsupport.utils.Utils.LazyNewInstance;

@SuppressWarnings("unchecked")
public class PacketIdMiddleTransformerRegistry<T> {

	private static final int listenerStateLength = Protocol.values().length;

	private final LazyNewInstance<T>[] registry = new LazyNewInstance[listenerStateLength * 256];
	private InitCallBack<T> callback;

	public void register(Protocol state, int packetId, Class<? extends T> packetTransformer) {
		registry[toKey(state, packetId)] = new LazyNewInstance<>(packetTransformer);
	}

	public void setCallBack(InitCallBack<T> callback) {
		this.callback = callback;
	}

	public T getTransformer(Protocol state, int packetId, boolean throwOnNull) throws InstantiationException, IllegalAccessException {
		LazyNewInstance<T> transformer = registry[toKey(state, packetId)];
		if (transformer == null) {
			if (throwOnNull) {
				throw new NoSuchElementException("No transformer found for state " + state + " and packet id " + packetId);
			} else {
				return null;
			}
		}
		T object = transformer.getInstance();
		if (callback != null) {
			callback.onInit(object);
		}
		return object;
	}

	static int toKey(Protocol protocol, int packetId) {
		return (protocol.ordinal() << 8) | packetId;
	}

	@FunctionalInterface
	public static interface InitCallBack<T> {
		public void onInit(T object);
	}

}
