package protocolsupport.api;

public enum ProtocolVersion {

	MINECRAFT_1_9(107),
	MINECRAFT_1_8(47),
	MINECRAFT_1_7_10(5),
	MINECRAFT_1_7_5(4),
	MINECRAFT_1_6_4(78),
	MINECRAFT_1_6_2(74),
	MINECRAFT_1_6_1(73),
	MINECRAFT_1_5_2(61),
	MINECRAFT_1_5_1(60),
	MINECRAFT_1_4_7(51),
	UNKNOWN(-1);

	private int id;

	ProtocolVersion(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static ProtocolVersion fromId(int id) {
		switch (id) {
			case 107: {
				return MINECRAFT_1_9;
			}
			case 47: {
				return MINECRAFT_1_8;
			}
			case 5: {
				return MINECRAFT_1_7_10;
			}
			case 4: {
				return MINECRAFT_1_7_5;
			}
			case 78: {
				return MINECRAFT_1_6_4;
			}
			case 74: {
				return MINECRAFT_1_6_2;
			}
			case 73: {
				return MINECRAFT_1_6_1;
			}
			case 61: {
				return MINECRAFT_1_5_2;
			}
			case 60: {
				return MINECRAFT_1_5_1;
			}
			case 51: {
				return MINECRAFT_1_4_7;
			}
		}
		return UNKNOWN;
	}

}