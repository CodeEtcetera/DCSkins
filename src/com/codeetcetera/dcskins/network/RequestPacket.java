/**
 * File: RequestPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public class RequestPacket extends DataOutPacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public RequestPacket(final byte compression, final byte dataType,
			final String user) throws IOException {
		super(compression, Packet.PACKETTYPE_REQ, dataType, user);
		finishPacket();
	}
}
