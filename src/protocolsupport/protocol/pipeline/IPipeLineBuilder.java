package protocolsupport.protocol.pipeline;

import java.util.Arrays;
import java.util.EnumMap;

import io.netty.channel.Channel;
import protocolsupport.api.Connection;
import protocolsupport.api.ProtocolVersion;

public abstract class IPipeLineBuilder {

	private static final IPipeLineBuilder noop = new IPipeLineBuilder() {
		@Override
		public void buildBungeeServer(Channel channel, Connection connection) {
		}
		@Override
		public void buildBungeeClientPipeLine(Channel channel, Connection connection) {
		}
		@Override
		public void buildBungeeClientCodec(Channel channel, Connection connection) {
		}
	};

	//TODO: a kick build for LEGACY versions
	public static final EnumMap<ProtocolVersion, IPipeLineBuilder> BUILDERS = new EnumMap<ProtocolVersion, IPipeLineBuilder>(ProtocolVersion.class);
	static {
		Arrays.stream(ProtocolVersion.getAllAfterI(ProtocolVersion.MINECRAFT_1_7_5)).forEach(version -> BUILDERS.put(version, noop));
		IPipeLineBuilder v6builder = new protocolsupport.protocol.pipeline.version.v_1_6.PipeLineBuilder();
		BUILDERS.put(ProtocolVersion.MINECRAFT_1_6_4, v6builder);
		BUILDERS.put(ProtocolVersion.MINECRAFT_1_6_2, v6builder);
		BUILDERS.put(ProtocolVersion.MINECRAFT_1_6_1, v6builder);
		IPipeLineBuilder v5builder = new protocolsupport.protocol.pipeline.version.v_1_5.PipeLineBuilder();
		BUILDERS.put(ProtocolVersion.MINECRAFT_1_5_2, v5builder);
		BUILDERS.put(ProtocolVersion.MINECRAFT_1_5_1, v5builder);
		BUILDERS.put(ProtocolVersion.MINECRAFT_1_4_7, new protocolsupport.protocol.pipeline.version.v_1_4.PipeLineBuilder());
		IPipeLineBuilder vpebuilder = new protocolsupport.protocol.pipeline.version.v_pe.PipeLineBuilder();
		BUILDERS.put(ProtocolVersion.MINECRAFT_PE_FUTURE, vpebuilder);
		BUILDERS.put(ProtocolVersion.MINECRAFT_PE, vpebuilder);
		BUILDERS.put(ProtocolVersion.MINECRAFT_PE_LEGACY, vpebuilder);
	}

	public abstract void buildBungeeClientCodec(Channel channel, Connection connection);

	public abstract void buildBungeeClientPipeLine(Channel channel, Connection connection);

	public abstract void buildBungeeServer(Channel channel, Connection connection);

}
