package protocolsupport.protocol.packet.middleimpl.writeable.handshake.v_pe;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.EncoderException;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.protocol.packet.id.PEPacketId;
import protocolsupport.protocol.packet.middleimpl.writeable.PESingleWriteablePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.utils.Utils;

public class LoginRequestServerHandshakePacket extends PESingleWriteablePacket<LoginRequest> {

	private static final KeyPair keypair = generateKeyPair();
	private static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator gen = KeyPairGenerator.getInstance("EC");
			gen.initialize(Curve.P_384.toECParameterSpec());
			return gen.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException("Unable to generate private keypair", e);
		}
	}

	public LoginRequestServerHandshakePacket() {
		super(PEPacketId.Serverbound.HANDSHAKE_LOGIN);
	}

	@Override
	protected void write(ByteBuf data, LoginRequest packet) {
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
		datachain.addProperty("identityPublicKey", Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded()));
		chainmap.put("chain", Collections.singletonList(encodeJWT(datachain)));
		return Utils.GSON.toJson(chainmap, new TypeToken<Map<String, List<String>>>() {}.getType());
	}

	private String createAuxData() {
		JsonObject clientinfo = new JsonObject();
		clientinfo.addProperty("ServerAddress", cache.getServerHandshake().getHost() + ":" + cache.getServerHandshake().getPort());
		clientinfo.addProperty("LanguageCode", cache.getLocale());
		clientinfo.addProperty("SkinData", cache.getSkinData());
		clientinfo.addProperty("SkinGeometryName", cache.getSkinGeometry());
		return encodeJWT(clientinfo);
	}

	private static String encodeJWT(JsonObject payload) {
		try {
			ECPrivateKey privatekey = (ECPrivateKey) keypair.getPrivate();
			JWSObject jwsobject = new JWSObject(
				new JWSHeader.Builder(JWSAlgorithm.ES384).x509CertURL(new URI(Base64.getEncoder().encodeToString(privatekey.getEncoded()))).build(),
				new Payload(Utils.GSON.toJson(payload))
			);
			jwsobject.sign(new ECDSASigner(privatekey));
			return jwsobject.serialize();
		} catch (Exception e) {
			throw new EncoderException("Unable to encode jwt", e);
		}
	}

}
