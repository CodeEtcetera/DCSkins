/**
 * File: DCSkinsBukkit.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;

import com.google.common.base.Throwables;

import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.DCSkinsLog;

/**
 * @author CodeEtcetera
 * 
 */
public class BukkitDCSkins extends JavaPlugin {
	private static BukkitDCSkins instance;
	
	private BukkitPacketSender packetSender;
	private BukkitServerProxy proxy;
	
	/**
	 * @return The instance of the bukkit DCSkins
	 */
	public static BukkitDCSkins getInstance() {
		return BukkitDCSkins.instance;
	}
	
	/**
	 * @throws Exception
	 * 
	 */
	public BukkitDCSkins() throws Exception {
		BukkitDCSkins.instance = this;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		new BukkitDCSLog();
		new BukkitConfig();
		packetSender = new BukkitPacketSender();
		try {
			proxy = new BukkitServerProxy(packetSender);
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		
		Messenger m = Bukkit.getMessenger();
		m.registerOutgoingPluginChannel(this, DCSkinsCore.PACKET_CHANNEL);
		m.registerIncomingPluginChannel(this, DCSkinsCore.PACKET_CHANNEL, proxy);
		getServer().getPluginManager().registerEvents(proxy, this);
		try {
			proxy.init();
		} catch(Exception e) {
			DCSkinsLog.severe("Error on plugin load: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {
		HandlerList.unregisterAll(this);
		try {
			proxy.onSave();
		} catch(IOException e) {
			DCSkinsLog.warning("Error on plugin unload: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
}
