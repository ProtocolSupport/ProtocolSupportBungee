package protocolsupport.protocol.typeremapper;

import java.text.MessageFormat;

public class LegacyDimension {

	public static int get(Object dimension) {
		if (dimension instanceof String) {
			return get(dimension);
		} else if (dimension instanceof Number) {
			return ((Number) dimension).intValue();
		} else {
			throw new IllegalArgumentException("Unknown dimension object " + dimension + " class " + dimension.getClass());
		}
	}

	public static int getId(String dimension) {
		switch (dimension) {
			case "overworld":
			case "minecraft:overworld": {
				return 0;
			}
			case "the_nether":
			case "minecraft:the_nether": {
				return -1;
			}
			case "the_end":
			case "minecraft:the_end": {
				return 1;
			}
			default: {
				throw new IllegalArgumentException(MessageFormat.format("Unknown dimension {0}", dimension));
			}
		}
	}

}
