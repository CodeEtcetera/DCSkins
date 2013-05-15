/**
 * File: ResponseOKPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public class ResponseOKPacket extends ResponsePacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public ResponseOKPacket(final byte compression, final byte dataType,
			final String user) throws IOException {
		super(compression, dataType, user, Packet.RESPONSE_OK);
		finishPacket();
	}
}
