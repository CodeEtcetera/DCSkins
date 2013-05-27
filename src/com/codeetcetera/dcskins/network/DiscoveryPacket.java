/**
 * File: DiscoveryPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;
import com.codeetcetera.dcskins.compression.CompressionRegistry;

/**
 * @author CodeEtcetera
 * 
 */
public class DiscoveryPacket extends AbstractOutPacket {
	/**
	 * @param compression
	 * @param packetType
	 * @throws IOException
	 */
	public DiscoveryPacket() throws IOException {
		super(null, Packet.PACKETTYPE_DSC);
		buildPacket((byte) 0);
		
		CompressionRegistry reg = CompressionRegistry.getInstance();
		out.writeByte(reg.getNumCompressions());
		for(byte i = 0; i < 16; i++) {
			CompressionEntry compression = reg.getCompressionLocal(i);
			if(compression != null) {
				out.writeUTF(compression.getCompression().getName());
			}
		}
		finishPacket();
	}
}
