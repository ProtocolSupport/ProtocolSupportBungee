package protocolsupport.utils.netty;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import io.netty.handler.codec.DecoderException;
import io.netty.util.Recycler;
import io.netty.util.Recycler.Handle;

public class Decompressor {

	private static final int maxPacketLength = 2 << 21;

	private final Inflater inflater = new Inflater();
	private final byte[] buffer = new byte[maxPacketLength];
	private final Handle<Decompressor> handle;
	protected Decompressor(Handle<Decompressor> handle) {
		this.handle = handle;
	}

	private static final Recycler<Decompressor> recycler = new Recycler<Decompressor>() {
		@Override
		protected Decompressor newObject(Handle<Decompressor> handle) {
			return new Decompressor(handle);
		}
	};

	public static Decompressor create() {
		return recycler.get();
	}

	public byte[] decompress(byte[] input) {
		inflater.setInput(input);
		try {
			int length = inflater.inflate(buffer);
			if (!inflater.finished()) {
				throw new DecoderException(MessageFormat.format("Badly compressed packet - size is larger than protocol maximum of {0}", maxPacketLength));
			}
			return Arrays.copyOf(buffer, length);
		} catch (DataFormatException e) {
			throw new RuntimeException(e);
		} finally {
			inflater.reset();
		}
	}

	public void recycle() {
		handle.recycle(this);
	}

	public static byte[] decompressStatic(byte[] input) {
		Decompressor decompressor = create();
		try {
			return decompressor.decompress(input);
		} finally {
			decompressor.recycle();
		}
	}

}
