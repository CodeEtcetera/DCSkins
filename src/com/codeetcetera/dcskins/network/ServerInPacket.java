/**
 * File: ServerInPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionRegistry;

/**
 * @author CodeEtcetera
 * 
 */
public class ServerInPacket extends AbstractInPacket {
	public ServerInPacket(final byte[] data) throws IOException {
		super(data);
		
		CompressionRegistry reg = CompressionRegistry.getInstance();
		compression = reg.getCompressionRemote(compressionIdx);
		
		// Wrap stream with correct decompression
		buildInputStream();
	}
}
