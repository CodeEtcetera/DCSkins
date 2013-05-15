/**
 * File: PacketSender.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionRegistry;
import com.codeetcetera.dcskins.compression.ICompressionStreamProvider;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class OutPacket extends Packet {
	private final ByteArrayOutputStream bout;
	protected final DataOutputStream out;
	
	public OutPacket(final byte compression, final byte packetType)
			throws IOException {
		this(Packet.PROTOCOL_VERSION, compression, packetType);
	}
	
	public OutPacket(final byte protocolVersion, final byte compression,
			final byte packetType) throws IOException {
		this.protocolVersion = protocolVersion;
		this.compression = compression;
		this.packetType = packetType;
		
		bout = new ByteArrayOutputStream();
		byte first = (byte) (protocolVersion << 4);
		first |= (compression & 0xF);
		bout.write(first);
		bout.write(packetType);
		
		ICompressionStreamProvider compressionProvider =
			CompressionRegistry.getInstance().getCompression(compression);
		out = new DataOutputStream(compressionProvider.getOutputStream(bout));
	}
	
	/**
	 * @return The data that makes this packet
	 */
	public byte[] getPacketData() {
		return bout.toByteArray();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.network.Packet#finishPacket()
	 */
	@Override
	public final void finishPacket() throws IOException {
		out.flush();
		out.close();
	}
}
