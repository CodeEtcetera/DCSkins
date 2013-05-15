/**
 * File: VerifyPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public class VerifyPacket extends DataOutPacket {
	/**
	 * @param request
	 * @throws IOException
	 */
	public VerifyPacket(final byte compression, final byte dataType,
			final String user, final String key) throws IOException {
		super(compression, Packet.PACKETTYPE_VFY, dataType, user);
		out.writeUTF(key);
		finishPacket();
	}
}
