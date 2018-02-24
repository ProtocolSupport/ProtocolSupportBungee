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
		public static final int PLAY_RESPAWN = 0x3D;

	}

	public static class Dualbound {

		public static final int PLAY_CHAT = 0x09;
		public static final int PLAY_PLAYER_MOVE_LOOK = 0x13;

	}

}
