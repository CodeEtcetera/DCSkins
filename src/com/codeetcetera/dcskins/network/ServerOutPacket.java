/**
 * File: ServerOutPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class ServerOutPacket extends AbstractOutPacket {
	public ServerOutPacket(final CompressionEntry compression,
			final byte packetType, final byte dataType, final String user)
			throws IOException {
		super(compression, packetType);
		buildPacket(compression.getLocalIdx());
		out.writeByte(dataType);
		out.writeUTF(user);
	}
}
