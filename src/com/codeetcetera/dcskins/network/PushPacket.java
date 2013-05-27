/**
 * File: PushPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public class PushPacket extends ClientOutPacket {
	/**
	 * @param compression
	 * @param dataType
	 * @param user
	 * @param data
	 * @throws IOException
	 */
	public PushPacket(final CompressionEntry compression, final byte dataType,
			final String user, final byte[] data) throws IOException {
		super(compression, Packet.PACKETTYPE_PSH, dataType, user);
		out.writeInt(data.length);
		out.write(data);
		finishPacket();
	}
}
