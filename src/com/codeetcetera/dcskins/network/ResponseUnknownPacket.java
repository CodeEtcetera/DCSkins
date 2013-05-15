/**
 * File: ResponseUnknownPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public class ResponseUnknownPacket extends ResponsePacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public ResponseUnknownPacket(final byte compression, final byte dataType,
			final String user) throws IOException {
		super(compression, dataType, user, Packet.RESPONSE_UNKNOWN);
		finishPacket();
	}
}
