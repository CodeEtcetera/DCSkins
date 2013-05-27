/**
 * File: AbstractOutPacket.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;
import com.codeetcetera.dcskins.compression.CompressionRegistry;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class AbstractOutPacket extends Packet {
	private final ByteArrayOutputStream bout;
	protected DataOutputStream out;
	
	/**
	 * @param compression
	 *            The compression entry of the compression that should be used.
	 *            Null for no compression
	 * @param packetType
	 *            The type of the packet
	 */
	public AbstractOutPacket(final CompressionEntry compression,
			final byte packetType) {
		this(Packet.PROTOCOL_VERSION, compression, packetType);
	}
	
	/**
	 * @param protocolVersion
	 *            The version of the protocol this packet is build for
	 * @param compression
	 *            The compression entry of the compression that should be used
	 *            Null for no compression.
	 * @param packetType
	 *            The type of the packet
	 */
	public AbstractOutPacket(final byte protocolVersion,
			final CompressionEntry compression, final byte packetType) {
		this.protocolVersion = protocolVersion;
		this.compression = compression;
		this.packetType = packetType;
		
		// No compression provided, use no compression
		if(compression == null) {
			this.compression =
				CompressionRegistry.getInstance().getCompressionLocal((byte) 0);
		}
		
		bout = new ByteArrayOutputStream();
	}
	
	protected void buildPacket(final byte compressionIdx) throws IOException {
		byte first = (byte) (protocolVersion << 4);
		first |= (compressionIdx & 0xF);
		bout.write(first);
		bout.write(packetType);
		
		out =
			new DataOutputStream(compression.getCompression().getOutputStream(
					bout));
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
