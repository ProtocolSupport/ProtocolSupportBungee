package protocolsupport.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.SwappedByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import io.netty.util.internal.StringUtil;

@SuppressWarnings("deprecation")
public final class ReplayingDecoderBuffer extends ByteBuf {

	private ByteBuf buffer;
	private SwappedByteBuf swapped;

	public ReplayingDecoderBuffer(final ByteBuf buffer) {
		this.setCumulation(buffer);
	}

	public void setCumulation(final ByteBuf buffer) {
		this.buffer = buffer;
	}

	@Override
	public boolean isReadable() {
		return this.buffer.isReadable();
	}

	@Override
	public boolean isReadable(final int size) {
		return this.buffer.isReadable(size);
	}

	@Override
	public int readableBytes() {
		return this.buffer.readableBytes();
	}

	@Override
	public int capacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public ByteBuf capacity(final int newCapacity) {
		throw reject();
	}

	@Override
	public int maxCapacity() {
		return this.capacity();
	}

	@Override
	public ByteBufAllocator alloc() {
		return this.buffer.alloc();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public ByteBuf asReadOnly() {
		return Unpooled.unmodifiableBuffer(this);
	}

	@Override
	public boolean isDirect() {
		return this.buffer.isDirect();
	}

	@Override
	public boolean hasArray() {
		return false;
	}

	@Override
	public byte[] array() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int arrayOffset() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasMemoryAddress() {
		return false;
	}

	@Override
	public long memoryAddress() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ByteBuf clear() {
		throw reject();
	}

	@Override
	public boolean equals(final Object obj) {
		return this == obj;
	}

	@Override
	public int compareTo(final ByteBuf buffer) {
		throw reject();
	}

	@Override
	public ByteBuf copy() {
		throw reject();
	}

	@Override
	public ByteBuf copy(final int index, final int length) {
		this.checkIndex(index, length);
		return this.buffer.copy(index, length);
	}

	@Override
	public ByteBuf discardReadBytes() {
		throw reject();
	}

	@Override
	public ByteBuf ensureWritable(final int writableBytes) {
		throw reject();
	}

	@Override
	public int ensureWritable(final int minWritableBytes, final boolean force) {
		throw reject();
	}

	@Override
	public ByteBuf duplicate() {
		throw reject();
	}

	@Override
	public ByteBuf retainedDuplicate() {
		throw reject();
	}

	@Override
	public boolean getBoolean(final int index) {
		this.checkIndex(index, 1);
		return this.buffer.getBoolean(index);
	}

	@Override
	public byte getByte(final int index) {
		this.checkIndex(index, 1);
		return this.buffer.getByte(index);
	}

	@Override
	public short getUnsignedByte(final int index) {
		this.checkIndex(index, 1);
		return this.buffer.getUnsignedByte(index);
	}

	@Override
	public ByteBuf getBytes(final int index, final byte[] dst, final int dstIndex, final int length) {
		this.checkIndex(index, length);
		this.buffer.getBytes(index, dst, dstIndex, length);
		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final byte[] dst) {
		this.checkIndex(index, dst.length);
		this.buffer.getBytes(index, dst);
		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuffer dst) {
		throw reject();
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuf dst, final int dstIndex, final int length) {
		this.checkIndex(index, length);
		this.buffer.getBytes(index, dst, dstIndex, length);
		return this;
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuf dst, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf getBytes(final int index, final ByteBuf dst) {
		throw reject();
	}

	@Override
	public int getBytes(final int index, final GatheringByteChannel out, final int length) {
		throw reject();
	}

	@Override
	public int getBytes(final int index, final FileChannel out, final long position, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf getBytes(final int index, final OutputStream out, final int length) {
		throw reject();
	}

	@Override
	public int getInt(final int index) {
		this.checkIndex(index, 4);
		return this.buffer.getInt(index);
	}

	@Override
	public int getIntLE(final int index) {
		this.checkIndex(index, 4);
		return this.buffer.getIntLE(index);
	}

	@Override
	public long getUnsignedInt(final int index) {
		this.checkIndex(index, 4);
		return this.buffer.getUnsignedInt(index);
	}

	@Override
	public long getUnsignedIntLE(final int index) {
		this.checkIndex(index, 4);
		return this.buffer.getUnsignedIntLE(index);
	}

	@Override
	public long getLong(final int index) {
		this.checkIndex(index, 8);
		return this.buffer.getLong(index);
	}

	@Override
	public long getLongLE(final int index) {
		this.checkIndex(index, 8);
		return this.buffer.getLongLE(index);
	}

	@Override
	public int getMedium(final int index) {
		this.checkIndex(index, 3);
		return this.buffer.getMedium(index);
	}

	@Override
	public int getMediumLE(final int index) {
		this.checkIndex(index, 3);
		return this.buffer.getMediumLE(index);
	}

	@Override
	public int getUnsignedMedium(final int index) {
		this.checkIndex(index, 3);
		return this.buffer.getUnsignedMedium(index);
	}

	@Override
	public int getUnsignedMediumLE(final int index) {
		this.checkIndex(index, 3);
		return this.buffer.getUnsignedMediumLE(index);
	}

	@Override
	public short getShort(final int index) {
		this.checkIndex(index, 2);
		return this.buffer.getShort(index);
	}

	@Override
	public short getShortLE(final int index) {
		this.checkIndex(index, 2);
		return this.buffer.getShortLE(index);
	}

	@Override
	public int getUnsignedShort(final int index) {
		this.checkIndex(index, 2);
		return this.buffer.getUnsignedShort(index);
	}

	@Override
	public int getUnsignedShortLE(final int index) {
		this.checkIndex(index, 2);
		return this.buffer.getUnsignedShortLE(index);
	}

	@Override
	public char getChar(final int index) {
		this.checkIndex(index, 2);
		return this.buffer.getChar(index);
	}

	@Override
	public float getFloat(final int index) {
		this.checkIndex(index, 4);
		return this.buffer.getFloat(index);
	}

	@Override
	public double getDouble(final int index) {
		this.checkIndex(index, 8);
		return this.buffer.getDouble(index);
	}

	@Override
	public CharSequence getCharSequence(final int index, final int length, final Charset charset) {
		this.checkIndex(index, length);
		return this.buffer.getCharSequence(index, length, charset);
	}

	@Override
	public int hashCode() {
		throw reject();
	}

	@Override
	public int indexOf(final int fromIndex, final int toIndex, final byte value) {
		if (fromIndex == toIndex) {
			return -1;
		}
		if (Math.max(fromIndex, toIndex) > this.buffer.writerIndex()) {
			throw EOSSignal.instance;
		}
		return this.buffer.indexOf(fromIndex, toIndex, value);
	}

	@Override
	public int bytesBefore(final byte value) {
		final int bytes = this.buffer.bytesBefore(value);
		if (bytes < 0) {
			throw EOSSignal.instance;
		}
		return bytes;
	}

	@Override
	public int bytesBefore(final int length, final byte value) {
		return this.bytesBefore(this.buffer.readerIndex(), length, value);
	}

	@Override
	public int bytesBefore(final int index, final int length, final byte value) {
		final int writerIndex = this.buffer.writerIndex();
		if (index >= writerIndex) {
			throw EOSSignal.instance;
		}
		if (index <= (writerIndex - length)) {
			return this.buffer.bytesBefore(index, length, value);
		}
		final int res = this.buffer.bytesBefore(index, writerIndex - index, value);
		if (res < 0) {
			throw EOSSignal.instance;
		}
		return res;
	}

	@Override
	public int forEachByte(final ByteProcessor processor) {
		final int ret = this.buffer.forEachByte(processor);
		if (ret < 0) {
			throw EOSSignal.instance;
		}
		return ret;
	}

	@Override
	public int forEachByte(final int index, final int length, final ByteProcessor processor) {
		final int writerIndex = this.buffer.writerIndex();
		if (index >= writerIndex) {
			throw EOSSignal.instance;
		}
		if (index <= (writerIndex - length)) {
			return this.buffer.forEachByte(index, length, processor);
		}
		final int ret = this.buffer.forEachByte(index, writerIndex - index, processor);
		if (ret < 0) {
			throw EOSSignal.instance;
		}
		return ret;
	}

	@Override
	public int forEachByteDesc(final ByteProcessor processor) {
		throw reject();
	}

	@Override
	public int forEachByteDesc(final int index, final int length, final ByteProcessor processor) {
		if ((index + length) > this.buffer.writerIndex()) {
			throw EOSSignal.instance;
		}
		return this.buffer.forEachByteDesc(index, length, processor);
	}

	@Override
	public ByteBuf markReaderIndex() {
		this.buffer.markReaderIndex();
		return this;
	}

	@Override
	public ByteBuf markWriterIndex() {
		throw reject();
	}

	@Override
	public ByteOrder order() {
		return this.buffer.order();
	}

	@Override
	public ByteBuf order(final ByteOrder endianness) {
		if (endianness == null) {
			throw new NullPointerException("endianness");
		}
		if (endianness == this.order()) {
			return this;
		}
		SwappedByteBuf swapped = this.swapped;
		if (swapped == null) {
			swapped = (this.swapped = new SwappedByteBuf(this));
		}
		return swapped;
	}

	@Override
	public boolean readBoolean() {
		this.checkReadableBytes(1);
		return this.buffer.readBoolean();
	}

	@Override
	public byte readByte() {
		this.checkReadableBytes(1);
		return this.buffer.readByte();
	}

	@Override
	public short readUnsignedByte() {
		this.checkReadableBytes(1);
		return this.buffer.readUnsignedByte();
	}

	@Override
	public ByteBuf readBytes(final byte[] dst, final int dstIndex, final int length) {
		this.checkReadableBytes(length);
		this.buffer.readBytes(dst, dstIndex, length);
		return this;
	}

	@Override
	public ByteBuf readBytes(final byte[] dst) {
		this.checkReadableBytes(dst.length);
		this.buffer.readBytes(dst);
		return this;
	}

	@Override
	public ByteBuf readBytes(final ByteBuffer dst) {
		throw reject();
	}

	@Override
	public ByteBuf readBytes(final ByteBuf dst, final int dstIndex, final int length) {
		this.checkReadableBytes(length);
		this.buffer.readBytes(dst, dstIndex, length);
		return this;
	}

	@Override
	public ByteBuf readBytes(final ByteBuf dst, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf readBytes(final ByteBuf dst) {
		this.checkReadableBytes(dst.writableBytes());
		this.buffer.readBytes(dst);
		return this;
	}

	@Override
	public int readBytes(final GatheringByteChannel out, final int length) {
		throw reject();
	}

	@Override
	public int readBytes(final FileChannel out, final long position, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf readBytes(final int length) {
		this.checkReadableBytes(length);
		return this.buffer.readBytes(length);
	}

	@Override
	public ByteBuf readSlice(final int length) {
		this.checkReadableBytes(length);
		return this.buffer.readSlice(length);
	}

	@Override
	public ByteBuf readRetainedSlice(final int length) {
		this.checkReadableBytes(length);
		return this.buffer.readRetainedSlice(length);
	}

	@Override
	public ByteBuf readBytes(final OutputStream out, final int length) {
		throw reject();
	}

	@Override
	public int readerIndex() {
		return this.buffer.readerIndex();
	}

	@Override
	public ByteBuf readerIndex(final int readerIndex) {
		this.buffer.readerIndex(readerIndex);
		return this;
	}

	@Override
	public int readInt() {
		this.checkReadableBytes(4);
		return this.buffer.readInt();
	}

	@Override
	public int readIntLE() {
		this.checkReadableBytes(4);
		return this.buffer.readIntLE();
	}

	@Override
	public long readUnsignedInt() {
		this.checkReadableBytes(4);
		return this.buffer.readUnsignedInt();
	}

	@Override
	public long readUnsignedIntLE() {
		this.checkReadableBytes(4);
		return this.buffer.readUnsignedIntLE();
	}

	@Override
	public long readLong() {
		this.checkReadableBytes(8);
		return this.buffer.readLong();
	}

	@Override
	public long readLongLE() {
		this.checkReadableBytes(8);
		return this.buffer.readLongLE();
	}

	@Override
	public int readMedium() {
		this.checkReadableBytes(3);
		return this.buffer.readMedium();
	}

	@Override
	public int readMediumLE() {
		this.checkReadableBytes(3);
		return this.buffer.readMediumLE();
	}

	@Override
	public int readUnsignedMedium() {
		this.checkReadableBytes(3);
		return this.buffer.readUnsignedMedium();
	}

	@Override
	public int readUnsignedMediumLE() {
		this.checkReadableBytes(3);
		return this.buffer.readUnsignedMediumLE();
	}

	@Override
	public short readShort() {
		this.checkReadableBytes(2);
		return this.buffer.readShort();
	}

	@Override
	public short readShortLE() {
		this.checkReadableBytes(2);
		return this.buffer.readShortLE();
	}

	@Override
	public int readUnsignedShort() {
		this.checkReadableBytes(2);
		return this.buffer.readUnsignedShort();
	}

	@Override
	public int readUnsignedShortLE() {
		this.checkReadableBytes(2);
		return this.buffer.readUnsignedShortLE();
	}

	@Override
	public char readChar() {
		this.checkReadableBytes(2);
		return this.buffer.readChar();
	}

	@Override
	public float readFloat() {
		this.checkReadableBytes(4);
		return this.buffer.readFloat();
	}

	@Override
	public double readDouble() {
		this.checkReadableBytes(8);
		return this.buffer.readDouble();
	}

	@Override
	public CharSequence readCharSequence(final int length, final Charset charset) {
		this.checkReadableBytes(length);
		return this.buffer.readCharSequence(length, charset);
	}

	@Override
	public ByteBuf resetReaderIndex() {
		this.buffer.resetReaderIndex();
		return this;
	}

	@Override
	public ByteBuf resetWriterIndex() {
		throw reject();
	}

	@Override
	public ByteBuf setBoolean(final int index, final boolean value) {
		throw reject();
	}

	@Override
	public ByteBuf setByte(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setBytes(final int index, final byte[] src, final int srcIndex, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf setBytes(final int index, final byte[] src) {
		throw reject();
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuffer src) {
		throw reject();
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuf src, final int srcIndex, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuf src, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf setBytes(final int index, final ByteBuf src) {
		throw reject();
	}

	@Override
	public int setBytes(final int index, final InputStream in, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf setZero(final int index, final int length) {
		throw reject();
	}

	@Override
	public int setBytes(final int index, final ScatteringByteChannel in, final int length) {
		throw reject();
	}

	@Override
	public int setBytes(final int index, final FileChannel in, final long position, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf setIndex(final int readerIndex, final int writerIndex) {
		throw reject();
	}

	@Override
	public ByteBuf setInt(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setIntLE(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setLong(final int index, final long value) {
		throw reject();
	}

	@Override
	public ByteBuf setLongLE(final int index, final long value) {
		throw reject();
	}

	@Override
	public ByteBuf setMedium(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setMediumLE(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setShort(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setShortLE(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setChar(final int index, final int value) {
		throw reject();
	}

	@Override
	public ByteBuf setFloat(final int index, final float value) {
		throw reject();
	}

	@Override
	public ByteBuf setDouble(final int index, final double value) {
		throw reject();
	}

	@Override
	public ByteBuf skipBytes(final int length) {
		this.checkReadableBytes(length);
		this.buffer.skipBytes(length);
		return this;
	}

	@Override
	public ByteBuf slice() {
		throw reject();
	}

	@Override
	public ByteBuf retainedSlice() {
		throw reject();
	}

	@Override
	public ByteBuf slice(final int index, final int length) {
		this.checkIndex(index, length);
		return this.buffer.slice(index, length);
	}

	@Override
	public ByteBuf retainedSlice(final int index, final int length) {
		this.checkIndex(index, length);
		return this.buffer.slice(index, length);
	}

	@Override
	public int nioBufferCount() {
		return this.buffer.nioBufferCount();
	}

	@Override
	public ByteBuffer nioBuffer() {
		throw reject();
	}

	@Override
	public ByteBuffer nioBuffer(final int index, final int length) {
		this.checkIndex(index, length);
		return this.buffer.nioBuffer(index, length);
	}

	@Override
	public ByteBuffer[] nioBuffers() {
		throw reject();
	}

	@Override
	public ByteBuffer[] nioBuffers(final int index, final int length) {
		this.checkIndex(index, length);
		return this.buffer.nioBuffers(index, length);
	}

	@Override
	public ByteBuffer internalNioBuffer(final int index, final int length) {
		this.checkIndex(index, length);
		return this.buffer.internalNioBuffer(index, length);
	}

	@Override
	public String toString(final int index, final int length, final Charset charset) {
		this.checkIndex(index, length);
		return this.buffer.toString(index, length, charset);
	}

	@Override
	public String toString(final Charset charsetName) {
		throw reject();
	}

	@Override
	public String toString() {
		return StringUtil.simpleClassName(this) + '(' + "ridx=" + this.readerIndex() + ", " + "widx="
				+ this.writerIndex() + ')';
	}

	@Override
	public boolean isWritable() {
		return false;
	}

	@Override
	public boolean isWritable(final int size) {
		return false;
	}

	@Override
	public int writableBytes() {
		return 0;
	}

	@Override
	public int maxWritableBytes() {
		return 0;
	}

	@Override
	public ByteBuf writeBoolean(final boolean value) {
		throw reject();
	}

	@Override
	public ByteBuf writeByte(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeBytes(final byte[] src, final int srcIndex, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf writeBytes(final byte[] src) {
		throw reject();
	}

	@Override
	public ByteBuf writeBytes(final ByteBuffer src) {
		throw reject();
	}

	@Override
	public ByteBuf writeBytes(final ByteBuf src, final int srcIndex, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf writeBytes(final ByteBuf src, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf writeBytes(final ByteBuf src) {
		throw reject();
	}

	@Override
	public int writeBytes(final InputStream in, final int length) {
		throw reject();
	}

	@Override
	public int writeBytes(final ScatteringByteChannel in, final int length) {
		throw reject();
	}

	@Override
	public int writeBytes(final FileChannel in, final long position, final int length) {
		throw reject();
	}

	@Override
	public ByteBuf writeInt(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeIntLE(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeLong(final long value) {
		throw reject();
	}

	@Override
	public ByteBuf writeLongLE(final long value) {
		throw reject();
	}

	@Override
	public ByteBuf writeMedium(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeMediumLE(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeZero(final int length) {
		throw reject();
	}

	@Override
	public int writerIndex() {
		return this.buffer.writerIndex();
	}

	@Override
	public ByteBuf writerIndex(final int writerIndex) {
		throw reject();
	}

	@Override
	public ByteBuf writeShort(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeShortLE(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeChar(final int value) {
		throw reject();
	}

	@Override
	public ByteBuf writeFloat(final float value) {
		throw reject();
	}

	@Override
	public ByteBuf writeDouble(final double value) {
		throw reject();
	}

	@Override
	public int setCharSequence(final int index, final CharSequence sequence, final Charset charset) {
		throw reject();
	}

	@Override
	public int writeCharSequence(final CharSequence sequence, final Charset charset) {
		throw reject();
	}

	private void checkIndex(final int index, final int length) {
		if ((index + length) > this.buffer.writerIndex()) {
			throw EOSSignal.instance;
		}
	}

	private void checkReadableBytes(final int readableBytes) {
		if (this.buffer.readableBytes() < readableBytes) {
			throw EOSSignal.instance;
		}
	}

	@Override
	public ByteBuf discardSomeReadBytes() {
		throw reject();
	}

	@Override
	public int refCnt() {
		return this.buffer.refCnt();
	}

	@Override
	public ByteBuf retain() {
		throw reject();
	}

	@Override
	public ByteBuf retain(final int increment) {
		throw reject();
	}

	@Override
	public ByteBuf touch() {
		this.buffer.touch();
		return this;
	}

	@Override
	public ByteBuf touch(final Object hint) {
		this.buffer.touch(hint);
		return this;
	}

	@Override
	public boolean release() {
		throw reject();
	}

	@Override
	public boolean release(final int decrement) {
		throw reject();
	}

	@Override
	public ByteBuf unwrap() {
		throw reject();
	}

	private static UnsupportedOperationException reject() {
		return new UnsupportedOperationException("not a replayable operation");
	}

	public static class EOSSignal extends RuntimeException {
		protected static final EOSSignal instance = new EOSSignal();
		private static final long serialVersionUID = 1L;

		@Override
		public synchronized Throwable fillInStackTrace() {
			return this;
		}
	}

}