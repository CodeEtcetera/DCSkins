/**
 * File: AbstractInPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class AbstractInPacket extends Packet {
	private final ByteArrayInputStream bin;
	protected DataInputStream in;
	protected final byte compressionIdx;
	
	public AbstractInPacket(final byte[] data) {
		bin = new ByteArrayInputStream(data);
		
		byte first = (byte) bin.read();
		protocolVersion = (byte) (first >> 4);
		compressionIdx = (byte) (first & 0xF);
		packetType = (byte) bin.read();
	}
	
	protected void buildInputStream() throws IOException {
		in =
			new DataInputStream(compression.getCompression()
											.getInputStream(bin));
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
