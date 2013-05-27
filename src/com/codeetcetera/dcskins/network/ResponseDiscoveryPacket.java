/**
 * File: DiscoveryResponsePacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionRegistry;

/**
 * @author CodeEtcetera
 * 
 */
public class ResponseDiscoveryPacket extends ResponsePacket {
	/**
	 * @param keyGen
	 *            The key generator we use
	 * @param compressions
	 *            An array of indices of the compressions we should send back
	 * @param num
	 *            The number of compressions <= compressions.length
	 * @throws IOException
	 */
	public ResponseDiscoveryPacket(final String keyGen,
			final byte[] compressions, final int num) throws IOException {
		super(null, (byte) 0, "", Packet.RESPONSE_DISCOVERY);
		out.writeUTF(keyGen);
		
		CompressionRegistry reg = CompressionRegistry.getInstance();
		out.writeByte(num);
		/*
		 * Write all the compressions we support plus the indices we use for
		 * them.
		 */
		for(byte i = 0; i < num; i++) {
			out.writeUTF(reg.getCompressionLocal(compressions[i])
							.getCompression().getName());
			out.writeByte(compressions[i]);
		}
	}
}
