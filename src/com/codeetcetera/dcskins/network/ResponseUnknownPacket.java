/**
 * File: ResponseUnknownPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public class ResponseUnknownPacket extends ResponsePacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public ResponseUnknownPacket(final CompressionEntry compression,
			final byte dataType, final String user) throws IOException {
		super(compression, dataType, user, Packet.RESPONSE_UNKNOWN);
		finishPacket();
	}
}
