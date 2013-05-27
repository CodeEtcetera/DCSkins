/**
 * File: ClientOutPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class ClientOutPacket extends AbstractOutPacket {
	public ClientOutPacket(final CompressionEntry compression,
			final byte packetType, final byte dataType, final String user)
			throws IOException {
		super(compression, packetType);
		buildPacket(compression.getRemoteIdx());
		out.writeByte(dataType);
		out.writeUTF(user);
	}
}
