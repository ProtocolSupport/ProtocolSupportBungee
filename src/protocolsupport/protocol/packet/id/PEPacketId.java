package protocolsupport.protocol.packet.id;

public class PEPacketId {

	public static class Serverbound {

		public static final int HANDSHAKE_LOGIN = 0x01;
		public static final int PLAY_COMMAND_REQEST = 0x4D;

	}

	public static class Clientbound {

		public static final int PLAY_PLAY_STATUS = 0x02;
		public static final int PLAY_KICK = 0x05;
		public static final int PLAY_RESOURCE_PACK = 0x06;
		public static final int PLAY_RESOURCE_STACK = 0x07;
		public static final int PLAY_START_GAME = 0x0B;
		public static final int PLAY_SPAWN_PLAYER = 0x0C;
		public static final int PLAY_ENTITY_SPAWN = 0x0D;
		public static final int PLAY_ENTITY_DESTROY = 0x0E;
		public static final int PLAY_ENTITY_COLLECT_EFFECT = 0x11;
		public static final int PLAY_ENTITY_TELEPORT = 0x12;
		public static final int PLAY_ENTITY_STATUS = 0x1B;
		public static final int PLAY_ENTITY_EFFECT = 0x1C;
		public static final int PLAY_ENTITY_ATTRIBUTES = 0x1D;
		public static final int PLAY_ENTITY_METADATA = 0x27;
		public static final int PLAY_ENTITY_VELOCITY = 0x28;
		public static final int PLAY_ENTITY_PASSENGER = 0x29;
		public static final int PLAY_ENTITY_ANIMATION = 0x2C;
		public static final int PLAY_RESPAWN = 0x3D;
		public static final int PLAY_PLAYER_GAME_TYPE = 0x3E;
		public static final int PLAY_PLAYER_INFO = 63;
	}

	public static class Dualbound {

		public static final int PLAY_CHAT = 0x09;
		public static final int PLAY_PLAYER_MOVE_LOOK = 0x13;

	}

}
