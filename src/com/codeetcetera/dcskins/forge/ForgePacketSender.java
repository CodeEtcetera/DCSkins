/**
 * File: ForgePacketSender.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.IPacketSender;
import com.codeetcetera.dcskins.network.AbstractInPacket;
import com.codeetcetera.dcskins.network.AbstractOutPacket;
import com.codeetcetera.dcskins.network.ServerOutPacket;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgePacketSender implements IPacketSender {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.IPacketSender#sendToServer(com.codeetcetera.
	 * dcskins.network.AbstractOutPacket)
	 */
	@Override
	public void sendToServer(final AbstractOutPacket packet) throws IOException {
		Packet250CustomPayload p = new Packet250CustomPayload();
		p.channel = DCSkinsCore.PACKET_CHANNEL;
		p.data = packet.getPacketData();
		p.length = p.data.length;
		PacketDispatcher.sendPacketToServer(p);
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
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = DCSkinsCore.PACKET_CHANNEL;
		packet.data = response.getPacketData();
		packet.length = packet.data.length;
		PacketDispatcher.sendPacketToPlayer(packet,
				((ForgeServerInPacket) inPacket).getPlayer());
	}
}
