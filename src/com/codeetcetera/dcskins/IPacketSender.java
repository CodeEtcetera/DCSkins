/**
 * File: IPacketSender.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.IOException;

import com.codeetcetera.dcskins.network.InPacket;
import com.codeetcetera.dcskins.network.OutPacket;

/**
 * @author CodeEtcetera
 * 
 */
public interface IPacketSender {
	/**
	 * @param packet
	 * @throws IOException
	 */
	public void sendToServer(OutPacket packet) throws IOException;
	
	/**
	 * @param response
	 * @param inPacket
	 * @throws IOException
	 */
	public void sendPlayerResponse(OutPacket response, InPacket inPacket)
			throws IOException;
}
