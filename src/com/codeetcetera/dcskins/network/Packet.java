/**
 * File: Packet.java
 * 
 */
package com.codeetcetera.dcskins.network;

import java.io.IOException;

import com.codeetcetera.dcskins.compression.CompressionEntry;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class Packet {
	public static final byte PROTOCOL_VERSION = 2;
	
	public static final byte PACKETTYPE_REQ = 0;
	public static final byte PACKETTYPE_PSH = 1;
	public static final byte PACKETTYPE_VFY = 2;
	public static final byte PACKETTYPE_RSP = 3;
	public static final byte PACKETTYPE_DSC = 4;
	
	public static final byte RESPONSE_OK = 1;
	public static final byte RESPONSE_DATA = 2;
	public static final byte RESPONSE_UNKNOWN = 3;
	public static final byte RESPONSE_DISCOVERY = 4;
	
	protected byte protocolVersion;
	protected CompressionEntry compression;
	protected byte packetType;
	
	/**
	 * @return True if this packet uses the correct protocol otherwise false
	 */
	public final boolean usesCorrectProtocol() {
		return protocolVersion == Packet.PROTOCOL_VERSION;
	}
	
	/**
	 * @return The (de)compression used for this packet
	 */
	public CompressionEntry getCompression() {
		return compression;
	}
	
	/**
	 * @return The packet type of this packet
	 */
	public byte getPacketType() {
		return packetType;
	}
	
	/**
	 * Finish access to this packet. This closes the streams used
	 * 
	 * @throws IOException
	 */
	public abstract void finishPacket() throws IOException;
}
