/**
 * File: DiscoveryPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionRegistry;
import com.codeetcetera.dcskins.compression.ICompressionStreamProvider;

/**
 * @author CodeEtcetera
 * 
 */
public class DiscoveryPacket extends OutPacket {
	/**
	 * @param compression
	 * @param packetType
	 * @throws IOException
	 */
	public DiscoveryPacket() throws IOException {
		super((byte) 0, Packet.PACKETTYPE_DSC);
		CompressionRegistry reg = CompressionRegistry.getInstance();
		out.writeByte(reg.getNumCompressions());
		for(byte i = 0; i < 16; i++) {
			ICompressionStreamProvider compression = reg.getCompression(i);
			if(compression != null) {
				out.writeUTF(compression.getName());
			}
		}
		finishPacket();
	}
}
