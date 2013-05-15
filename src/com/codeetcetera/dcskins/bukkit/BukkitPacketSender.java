/**
 * File: BukkitPacketSender.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.io.IOException;

import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.IPacketSender;
import com.codeetcetera.dcskins.network.InPacket;
import com.codeetcetera.dcskins.network.OutPacket;

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
	 * dcskins.network.OutPacket)
	 */
	@Override
	public void sendToServer(final OutPacket packet) throws IOException {
		throw new IOException("Not implemented on bukkit");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.IPacketSender#sendPlayerResponse(com.codeetcetera
	 * .dcskins.network.OutPacket, com.codeetcetera.dcskins.network.InPacket)
	 */
	@Override
	public void sendPlayerResponse(final OutPacket response,
			final InPacket inPacket) throws IOException {
		((BukkitInPacket) inPacket).getPlayer().sendPluginMessage(
				BukkitDCSkins.getInstance(), DCSkinsCore.PACKET_CHANNEL,
				response.getPacketData());
	}
	
}
