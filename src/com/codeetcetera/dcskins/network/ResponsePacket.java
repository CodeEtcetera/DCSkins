/**
 * File: ResponsePacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class ResponsePacket extends ServerOutPacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public ResponsePacket(final CompressionEntry compression,
			final byte dataType, final String user, final byte responseType)
			throws IOException {
		super(compression, Packet.PACKETTYPE_RSP, dataType, user);
		out.writeByte(responseType);
	}
}
