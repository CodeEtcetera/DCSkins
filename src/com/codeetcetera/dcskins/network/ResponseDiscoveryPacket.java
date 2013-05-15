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
	 * @param compressions
	 * @param num
	 *            The number of compressions <= compressions.length
	 * @throws IOException
	 */
	public ResponseDiscoveryPacket(final String keyGen,
			final byte[] compressions, final int num) throws IOException {
		super((byte) 0, (byte) 0, "", Packet.RESPONSE_DISCOVERY);
		out.writeUTF(keyGen);
		
		CompressionRegistry reg = CompressionRegistry.getInstance();
		out.writeByte(num);
		for(byte i = 0; i < num; i++) {
			out.writeUTF(reg.getCompression(compressions[i]).getName());
			out.writeByte(compressions[i]);
		}
	}
}
