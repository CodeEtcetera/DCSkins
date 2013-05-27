/**
 * File: IPacketSender.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.IOException;

import com.codeetcetera.dcskins.network.AbstractInPacket;
import com.codeetcetera.dcskins.network.AbstractOutPacket;
import com.codeetcetera.dcskins.network.ServerOutPacket;

/**
 * @author CodeEtcetera
 * 
 */
public interface IPacketSender {
	/**
	 * @param packet
	 * @throws IOException
	 */
	public void sendToServer(AbstractOutPacket packet) throws IOException;
	
	/**
	 * @param response
	 * @param inPacket
	 * @throws IOException
	 */
	public void sendPlayerResponse(ServerOutPacket response,
			AbstractInPacket inPacket) throws IOException;
}
