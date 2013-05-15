/**
 * File: ResponsePacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class ResponsePacket extends DataOutPacket {
	private byte responseType;
	
	/**
	 * @param request
	 * @throws IOException
	 */
	public ResponsePacket(final byte compression, final byte dataType,
			final String user, final byte responseType) throws IOException {
		super(compression, Packet.PACKETTYPE_RSP, dataType, user);
		out.writeByte(responseType);
	}
}
