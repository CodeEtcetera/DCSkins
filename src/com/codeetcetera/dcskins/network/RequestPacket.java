/**
 * File: RequestPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public class RequestPacket extends ClientOutPacket {
	/**
	 * @param compression
	 * @param dataType
	 * @param user
	 * @throws IOException
	 */
	public RequestPacket(final CompressionEntry compression,
			final byte dataType, final String user) throws IOException {
		super(compression, Packet.PACKETTYPE_REQ, dataType, user);
		finishPacket();
	}
}
