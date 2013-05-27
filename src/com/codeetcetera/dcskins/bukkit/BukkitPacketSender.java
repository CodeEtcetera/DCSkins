/**
 * File: BukkitPacketSender.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.io.IOException;

import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.IPacketSender;
import com.codeetcetera.dcskins.network.AbstractInPacket;
import com.codeetcetera.dcskins.network.AbstractOutPacket;
import com.codeetcetera.dcskins.network.ServerOutPacket;

/**
 * @author CodeEtcetera
 * 
 */
public class BukkitPacketSender implements IPacketSender {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.IPacketSender#sendToServer(com.codeetcetera.
	 * dcskins.network.AbstractOutPacket)
	 */
	@Override
	public void sendToServer(final AbstractOutPacket packet) throws IOException {
		throw new IOException(
				"Sending packets to server is not implemented for bukkit");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.IPacketSender#sendPlayerResponse(com.codeetcetera
	 * .dcskins.network.ServerOutPacket,
	 * com.codeetcetera.dcskins.network.AbstractInPacket)
	 */
	@Override
	public void sendPlayerResponse(final ServerOutPacket response,
			final AbstractInPacket inPacket) throws IOException {
		((BukkitInPacket) inPacket).getPlayer().sendPluginMessage(
				BukkitDCSkins.getInstance(), DCSkinsCore.PACKET_CHANNEL,
				response.getPacketData());
	}
	
}
