/**
 * File: VerifyPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public class VerifyPacket extends ClientOutPacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public VerifyPacket(final CompressionEntry compression,
			final byte dataType, final String user, final String key)
			throws IOException {
		super(compression, Packet.PACKETTYPE_VFY, dataType, user);
		out.writeUTF(key);
		finishPacket();
	}
}
