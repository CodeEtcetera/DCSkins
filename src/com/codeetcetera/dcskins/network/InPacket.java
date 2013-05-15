/**
 * File: InPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionRegistry;
import com.codeetcetera.dcskins.compression.ICompressionStreamProvider;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class InPacket extends Packet {
	private final ByteArrayInputStream bin;
	protected final DataInputStream in;
	
	public InPacket(final byte[] data) throws IOException {
		bin = new ByteArrayInputStream(data);
		byte first = (byte) bin.read();
		protocolVersion = (byte) (first >> 4);
		compression = (byte) (first & 0xF);
		packetType = (byte) bin.read();
		
		// Wrap stream with correct decompression
		ICompressionStreamProvider compressionProvider =
			CompressionRegistry.getInstance().getCompression(compression);
		in = new DataInputStream(compressionProvider.getInputStream(bin));
	}
	
	/**
	 * @return The already decompressed data stream which reads this packet
	 */
	public DataInputStream getStream() {
		return in;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.network.Packet#finishPacket()
	 */
	@Override
	public void finishPacket() throws IOException {
		in.close();
	}
}
