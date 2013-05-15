/**
 * File: BukkitInPacket.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.io.IOException;

import org.bukkit.entity.Player;

import com.codeetcetera.dcskins.network.InPacket;

/**
 * @author CodeEtcetera
 * 
 */
public class BukkitInPacket extends InPacket {
	private final Player player;
	
	/**
	 * @param data
	 * @throws IOException
	 */
	public BukkitInPacket(final byte[] data, final Player player)
			throws IOException {
		super(data);
		this.player = player;
	}
	
	/**
	 * @return The player that send this packet
	 */
	public Player getPlayer() {
		return player;
	}
}
