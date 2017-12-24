package protocolsupport.protocol.packet.middleimpl.readable.handshake.v_pe;

import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.factories.DefaultJWSVerifierFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import net.md_5.bungee.protocol.PacketWrapper;
import net.md_5.bungee.protocol.packet.Handshake;
import net.md_5.bungee.protocol.packet.LoginRequest;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.utils.Any;
import protocolsupport.protocol.packet.middleimpl.readable.PEDefinedReadableMiddlePacket;
import protocolsupport.protocol.serializer.ArraySerializer;
import protocolsupport.protocol.serializer.MiscSerializer;
import protocolsupport.utils.JsonUtils;
import protocolsupport.utils.Utils;

public class LoginHandshakePacket extends PEDefinedReadableMiddlePacket {

	public static final String XUID_METADATA_KEY = "___PS_PE_XUID";

	public static final int PACKET_ID = 1;

	private String username;
	protected String host;
	protected int port;

	public LoginHandshakePacket() {
		super(PACKET_ID);
	}

	@Override
	public Collection<PacketWrapper> toNative() {
		return Arrays.asList(
			new PacketWrapper(new Handshake(ProtocolVersion.MINECRAFT_1_7_10.getId(), host, port, 2), Unpooled.wrappedBuffer(readbytes)),
			new PacketWrapper(new LoginRequest(username), Unpooled.EMPTY_BUFFER)
		);
	}

	@SuppressWarnings("serial")
	@Override
	protected void read0(ByteBuf clientdata) {
		clientdata.readInt();
		ByteBuf logindata = Unpooled.wrappedBuffer(ArraySerializer.readVarIntLengthByteArray(clientdata));
		try {
			Any<Key, JsonObject> chaindata = extractChainData(Utils.GSON.fromJson(
				new InputStreamReader(new ByteBufInputStream(logindata, logindata.readIntLE())),
				new TypeToken<Map<String, List<String>>>() {}.getType()
			));
			username = JsonUtils.getString(chaindata.getObj2(), "displayName");
			cache.setPEClientUUID(UUID.fromString(JsonUtils.getString(chaindata.getObj2(), "identity")));
			if (chaindata.getObj1() != null) {
				connection.addMetadata(XUID_METADATA_KEY, JsonUtils.getString(chaindata.getObj2(), "XUID"));
			}
			JWSObject additionaldata = JWSObject.parse(new String(MiscSerializer.readBytes(logindata, logindata.readIntLE())));
			Map<String, String> clientinfo = Utils.GSON.fromJson(additionaldata.getPayload().toString(), new TypeToken<Map<String, String>>() {}.getType());
			String rserveraddress = clientinfo.get("ServerAddress");
			if (rserveraddress == null) {
				throw new DecoderException("ServerAddress is missing");
			}
			String[] rserveraddresssplit = rserveraddress.split("[:]");
			host = rserveraddresssplit[0];
			port = Integer.parseInt(rserveraddresssplit[1]);
			cache.setLocale(clientinfo.get("LanguageCode"));
		} catch (ParseException e) {
			throw new DecoderException("Unable to parse jwt", e);
		}
	}

	private static final String MOJANG_KEY = "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V";

	private static Any<Key, JsonObject> extractChainData(Map<String, List<String>> maindata) throws ParseException {
		List<String> chain = maindata.get("chain");
		try {
			PublicKey key = parseKey(MOJANG_KEY);
			boolean foundMojangKey = false;
			boolean signatureValid = false;
			for (String element : chain) {
				JWSObject jwsobject = JWSObject.parse(element);
				if (!foundMojangKey && jwsobject.getHeader().getX509CertURL().toString().equals(MOJANG_KEY)) {
					foundMojangKey = true;
					signatureValid = true;
				}
				if (foundMojangKey && !verify(jwsobject, key)) {
					signatureValid = false;
				}
				JsonObject jsonobject = Utils.GSON.fromJson(jwsobject.getPayload().toString(), JsonObject.class);
				key = parseKey(JsonUtils.getString(jsonobject, "identityPublicKey"));
				if (jsonobject.has("extraData")) {
					return new Any<Key, JsonObject>(signatureValid ? key : null, JsonUtils.getJsonObject(jsonobject, "extraData"));
				}
			}
		} catch (InvalidKeySpecException | JOSEException e) {
			throw new DecoderException("Unable to decode login chain", e);
		}
		throw new DecoderException("Unable to find extraData");
	}

	private static final DefaultJWSVerifierFactory jwsverifierfactory = new DefaultJWSVerifierFactory();
	private static boolean verify(JWSObject object, PublicKey key) throws JOSEException {
		return object.verify(jwsverifierfactory.createJWSVerifier(object.getHeader(), key));
	}

	private static final KeyFactory keyfactory = getKeyFactory();
	private static KeyFactory getKeyFactory() {
		try {
			return KeyFactory.getInstance("EC");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Unable to init key factory", e);
		}
	}

	private static PublicKey parseKey(String key) throws InvalidKeySpecException {
		return keyfactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(key)));
	}

}
