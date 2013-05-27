/**
 * File: ForgeServerInPacket.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;

import com.codeetcetera.dcskins.network.ServerInPacket;

import cpw.mods.fml.common.network.Player;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeServerInPacket extends ServerInPacket {
	private final Player player;
	
	/**
	 * @param data
	 * @throws IOException
	 */
	public ForgeServerInPacket(final byte[] data, final Player p)
			throws IOException {
		super(data);
		player = p;
	}
	
	/**
	 * @return The player that send this packet
	 */
	public Player getPlayer() {
		return player;
	}
}
