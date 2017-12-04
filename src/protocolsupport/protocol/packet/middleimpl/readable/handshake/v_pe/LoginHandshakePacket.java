package protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.utils.Utils;

public class LoginHandshakePacket extends PEDefinedReadableMiddlePacket {

	public static final int PACKET_ID = 1;

	private String username;

	public LoginHandshakePacket() {
		super(PACKET_ID);
	}

	@Override
	protected void read0(ByteBuf from) {
		from.readInt(); //protocol version
		ByteBuf logindata = Unpooled.wrappedBuffer(ArraySerializer.readVarIntLengthByteArray(from));
		//decode chain
		@SuppressWarnings("serial")
		Map<String, List<String>> map = Utils.GSON.fromJson(
			new InputStreamReader(new ByteBufInputStream(logindata, logindata.readIntLE())),
			new TypeToken<Map<String, List<String>>>() {}.getType()
		);
		for (String c : map.get("chain")) {
			JsonObject chainMap = decodeToken(c);
			if ((chainMap != null) && chainMap.has("extraData")) {
				JsonObject extra = chainMap.get("extraData").getAsJsonObject();
				if (extra.has("displayName")) {
					username = extra.get("displayName").getAsString();
				}
			}
		}
		//skip skin data
		logindata.skipBytes(logindata.readIntLE());
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Arrays.asList(
			new PacketWrapper(new Handshake(ProtocolVersion.MINECRAFT_1_7_10.getId(), "", 1, 2), Unpooled.wrappedBuffer(readbytes)),
			new PacketWrapper(new LoginRequest(username), Unpooled.EMPTY_BUFFER)
		);
	}

	private JsonObject decodeToken(String token) {
		String[] base = token.split("\\.");
		if (base.length < 2) {
			return null;
		}
		return Utils.GSON.fromJson(new InputStreamReader(new ByteArrayInputStream(Base64.getDecoder().decode(base[1]))), JsonObject.class);
	}

}
