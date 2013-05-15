/**
 * File: PushPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public class PushPacket extends DataOutPacket {
	/**
	 * @param compression
	 * @param packetType
	 * @param dataType
	 * @param user
	 * @throws IOException
	 */
	public PushPacket(final byte compression, final byte dataType,
			final String user, final byte[] data) throws IOException {
		super(compression, Packet.PACKETTYPE_PSH, dataType, user);
		out.writeInt(data.length);
		out.write(data);
		finishPacket();
	}
}
