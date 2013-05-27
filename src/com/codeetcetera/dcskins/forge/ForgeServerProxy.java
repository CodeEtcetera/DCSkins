/**
 * File: ForgeServerProxy.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import com.google.common.base.Throwables;

import com.codeetcetera.dcskins.CommonServer;
import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.DCSkinsLog;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeServerProxy extends CommonServer implements IPacketHandler {
	/**
	 * @throws Exception
	 */
	public ForgeServerProxy() throws Exception {
		packetSender = new ForgePacketSender();
		// Register ForgeSubscrive events
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.instance().registerChannel(this,
				DCSkinsCore.PACKET_CHANNEL);
	}
	
	/**
	 * World save handler, save the cache on world save
	 * 
	 * @param event
	 * @throws IOException
	 */
	@ForgeSubscribe
	public void onSave(final WorldEvent.Save event) throws IOException {
		onSave();
	}
	
	/**
	 * World unload handler, save the cache on unload
	 * 
	 * @param event
	 * @throws IOException
	 */
	@ForgeSubscribe
	public void onWorldUnload(final WorldEvent.Unload event) throws IOException {
		onSave();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IPacketHandler#onPacketData(net.minecraft
	 * .network.INetworkManager,
	 * net.minecraft.network.packet.Packet250CustomPayload,
	 * cpw.mods.fml.common.network.Player)
	 */
	@Override
	public void onPacketData(final INetworkManager manager,
			final Packet250CustomPayload packet, final Player player) {
		try {
			onIncomingPacket(new ForgeServerInPacket(packet.data, player));
		} catch(IOException e) {
			DCSkinsLog.warning("Error on incoming packet: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
}
