/**
 * File: ResponseDataPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public class ResponseDataPacket extends ResponsePacket {
	/**
	 * @param request
	 * @param data
	 * @throws IOException
	 */
	public ResponseDataPacket(final CompressionEntry compression,
			final byte dataType, final String user, final byte[] data)
			throws IOException {
		super(compression, dataType, user, Packet.RESPONSE_DATA);
		out.writeInt(data.length);
		out.write(data);
		finishPacket();
	}
}
