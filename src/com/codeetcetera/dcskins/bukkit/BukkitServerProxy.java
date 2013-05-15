/**
 * File: BukkitServerProxy.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.base.Throwables;

import com.codeetcetera.dcskins.CommonServer;
import com.codeetcetera.dcskins.DCSkinsLog;

/**
 * @author CodeEtcetera
 * 
 */
public class BukkitServerProxy extends CommonServer implements
		PluginMessageListener, Listener {
	
	/**
	 * @throws Exception
	 */
	public BukkitServerProxy(final BukkitPacketSender packetSender)
			throws Exception {
		this.packetSender = packetSender;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bukkit.plugin.messaging.PluginMessageListener#onPluginMessageReceived
	 * (java.lang.String, org.bukkit.entity.Player, byte[])
	 */
	@Override
	public void onPluginMessageReceived(final String channel,
			final Player player, final byte[] data) {
		try {
			onIncomingPacket(new BukkitInPacket(data, player));
		} catch(IOException e) {
			DCSkinsLog.warning("Error on incoming packet: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
	
	@EventHandler
	public void onWorldSave(final WorldSaveEvent event) throws IOException {
		onSave();
	}
	
	@EventHandler
	public void onWorldunload(final WorldUnloadEvent event) throws IOException {
		onSave();
	}
}
