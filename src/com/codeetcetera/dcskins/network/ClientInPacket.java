package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionRegistry;

public class ClientInPacket extends AbstractInPacket {
	public ClientInPacket(final byte[] data) throws IOException {
		super(data);
		
		CompressionRegistry reg = CompressionRegistry.getInstance();
		compression = reg.getCompressionLocal(compressionIdx);
		
		// Wrap stream with correct decompression
		buildInputStream();
	}
}
