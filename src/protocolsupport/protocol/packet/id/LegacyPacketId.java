package protocolsupport.protocol.packet.id;

public class LegacyPacketId {

	public static class Serverbound {

		public static final int HANDSHAKE_LOGIN = 0x02;
		public static final int HANDSHAKE_PING = 0xFE;
		public static final int LOGIN_ENCRYPTION_RESPONSE = 0xFC;
		public static final int LOGIN_PLAY_CLIENT_COMMAND = 0xCD;
		public static final int PLAY_USE_ENTITY = 0x07;
		public static final int PLAY_FLYING = 0x0A;
		public static final int PLAY_POSITION = 0x0B;
		public static final int PLAY_LOOK = 0x0C;
		public static final int PLAY_POSITION_LOOK = 0x0D;
		public static final int PLAY_BLOCK_DIG = 0x0E;
		public static final int PLAY_BLOCK_PLACE = 0x0F;
		public static final int PLAY_HELD_SLOT = 0x10;
		public static final int PLAY_VEHICLE_CONTROL = 0x1B;
		public static final int PLAY_INVENTORY_CLOSE = 0x65;
		public static final int PLAY_INVENTORY_CLICK = 0x66;
		public static final int PLAY_INVENTORY_CREATIVE_SET_SLOT = 0x6B;
		public static final int PLAY_INVENTORY_SELECT_ENCHANT = 0x6C;
		public static final int PLAY_CLIENT_SETTINGS = 0xCC;

	}

	public static class Clientbound {

		public static final int LOGIN_ENCRYPTION_REQUEST = 0xFD;
		public static final int PLAY_START_GAME = 0x01;
		public static final int PLAY_RESPAWN = 0x09;
		public static final int PLAY_PLAYER_LIST = 0xC9;
		public static final int PLAY_SCOREBOARD_OBJECTIVE = 0xCE;
		public static final int PLAY_SCOREBOARD_SCORE = 0xCF;
		public static final int PLAY_SCOREBOARD_DISPLAY_SLOT = 0xD0;
		public static final int PLAY_SCOREBOARD_TEAM = 0xD1;

	}

	public static class Dualbound {

		public static final int PLAY_KEEP_ALIVE = 0x00;
		public static final int PLAY_CHAT = 0x03;
		public static final int PLAY_ANIMATION = 0x12;
		public static final int PLAY_ENTITY_ACTION = 0x13;
		public static final int PLAY_INVENTORY_TRANSACTION = 0x6A;
		public static final int PLAY_UPDATE_SIGN = 0x82;
		public static final int PLAY_TAB_COMPLETE = 0xCB;
		public static final int PLAY_ABILITIES = 0xCA;
		public static final int PLAY_PLUGIN_MESSAGE = 0xFA;
		public static final int PLAY_KICK = 0xFF;

	}

}
