/**
 * File: DataOutPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class DataOutPacket extends OutPacket {
	/**
	 * @param compression
	 * @param packetType
	 * @throws IOException
	 */
	public DataOutPacket(final byte compression, final byte packetType,
			final byte dataType, final String user) throws IOException {
		super(compression, packetType);
		out.writeByte(dataType);
		out.writeUTF(user);
	}
}
