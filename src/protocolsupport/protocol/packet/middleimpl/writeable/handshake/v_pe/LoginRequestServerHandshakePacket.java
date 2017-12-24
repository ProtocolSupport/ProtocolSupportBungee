package protocolsupport.protocol.packet.middleimpl.writeable.handshake.v_pe;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.utils.Utils;

public class LoginRequestServerHandshakePacket extends PESingleWriteablePacket<LoginRequest> {

	public LoginRequestServerHandshakePacket() {
		super(1);
	}

	@Override
	protected void write(ByteBuf data, LoginRequest packet) {
		//TODO: actually write a proper jwt
		data.writeInt(connection.getVersion().getId());
		ByteBuf jwtdata = Unpooled.buffer();
		byte[] identitydata = createIdentityData(packet.getData()).getBytes(StandardCharsets.UTF_8);
		jwtdata.writeIntLE(identitydata.length);
		jwtdata.writeBytes(identitydata);
		byte[] auxdata = createAuxData().getBytes(StandardCharsets.UTF_8);
		jwtdata.writeIntLE(auxdata.length);
		jwtdata.writeBytes(auxdata);
		ArraySerializer.writeVarIntLengthByteArray(data, jwtdata);
	}

	@SuppressWarnings("serial")
	private String createIdentityData(String username) {
		Map<String, List<String>> chainmap = new HashMap<>();
		JsonObject datachain = new JsonObject();
		JsonObject extradata = new JsonObject();
		extradata.addProperty("displayName", username);
		extradata.addProperty("identity", cache.getPEClientUUID().toString());
		datachain.add("extraData", extradata);
		chainmap.put("chain", Collections.singletonList(encodeToken(datachain)));
		return Utils.GSON.toJson(chainmap, new TypeToken<Map<String, List<String>>>() {}.getType());
	}

	private String createAuxData() {
		JsonObject clientinfo = new JsonObject();
		clientinfo.addProperty("ServerAddress", cache.serverHandshake.getHost() + ":" + cache.serverHandshake.getPort());
		return encodeToken(clientinfo);
	}

	private String encodeToken(JsonObject token) {
		return "not_a_real_jwt." + Base64.getEncoder().encodeToString(Utils.GSON.toJson(token).getBytes(StandardCharsets.UTF_8));
	}

}
