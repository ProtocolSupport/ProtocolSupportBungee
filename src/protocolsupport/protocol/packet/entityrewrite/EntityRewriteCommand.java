package protocolsupport.protocol.packet.entityrewrite;

import java.util.function.IntUnaryOperator;

import io.netty.buffer.ByteBuf;
import protocolsupport.protocol.serializer.TypeCopier;
import protocolsupport.protocol.serializer.VarNumberSerializer;

public abstract class EntityRewriteCommand {

	protected abstract void rewrite(ByteBuf from, ByteBuf to, IntUnaryOperator rewritefunc);

	public static abstract class EntityIdEntityRewriteCommand extends EntityRewriteCommand {

		@Override
		protected void rewrite(ByteBuf from, ByteBuf to, IntUnaryOperator rewritefunc) {
			writeEntityId(to, rewritefunc.applyAsInt(readEntityId(from)));
		}

		protected abstract int readEntityId(ByteBuf from);

		protected abstract void writeEntityId(ByteBuf to, int entityId);

	}

	public static final EntityIdEntityRewriteCommand VARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND = new EntityIdEntityRewriteCommand() {
		@Override
		protected int readEntityId(ByteBuf from) {
			return (int) VarNumberSerializer.readVarLong(from);
		}
		@Override
		protected void writeEntityId(ByteBuf to, int entityId) {
			VarNumberSerializer.writeVarLong(to, entityId);
		}
	};

	public static final EntityIdEntityRewriteCommand INT_ENTITY_ID_ENTITY_REWRITE_COMMAND = new EntityIdEntityRewriteCommand() {
		@Override
		protected void writeEntityId(ByteBuf to, int entityId) {
			to.writeInt(entityId);
		}
		@Override
		protected int readEntityId(ByteBuf from) {
			return from.readInt();
		}
	};

	public static final EntityIdEntityRewriteCommand SVARLONG_ENTITY_ID_ENTITY_REWRITE_COMMAND = new EntityIdEntityRewriteCommand() {
		@Override
		protected int readEntityId(ByteBuf from) {
			return (int) VarNumberSerializer.readSVarLong(from);
		}
		@Override
		protected void writeEntityId(ByteBuf to, int entityId) {
			VarNumberSerializer.writeSVarLong(to, entityId);
		}
	};

	public static abstract class NoEntityIdRewriteEntityRewriteCommand extends EntityRewriteCommand {

		@Override
		protected void rewrite(ByteBuf from, ByteBuf to, IntUnaryOperator rewritefunc) {
			rewrite(from, to);
		}

		protected abstract void rewrite(ByteBuf from, ByteBuf to);

	}

	public static final EntityRewriteCommand REMAINING_BYTES_COPY_ENTITY_REWRITE_COMMAND = new NoEntityIdRewriteEntityRewriteCommand() {
		@Override
		protected void rewrite(ByteBuf from, ByteBuf to) {
			to.writeBytes(from);
		}
	};

	public static class FixedLengthBytesCopyEntityRewriteCommand extends NoEntityIdRewriteEntityRewriteCommand {

		protected final int length;
		public FixedLengthBytesCopyEntityRewriteCommand(int length) {
			this.length = length;
		}

		@Override
		public void rewrite(ByteBuf from, ByteBuf to) {
			TypeCopier.copyBytes(from, to, length);
		}

	}

}