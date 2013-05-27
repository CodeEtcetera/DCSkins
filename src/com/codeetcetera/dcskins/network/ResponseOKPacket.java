/**
 * File: ResponseOKPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public class ResponseOKPacket extends ResponsePacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public ResponseOKPacket(final CompressionEntry compression,
			final byte dataType, final String user) throws IOException {
		super(compression, dataType, user, Packet.RESPONSE_OK);
		finishPacket();
	}
}
