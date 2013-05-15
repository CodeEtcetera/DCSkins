/**
 * File: ForgeInPacket.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;

import com.codeetcetera.dcskins.network.InPacket;

import cpw.mods.fml.common.network.Player;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeInPacket extends InPacket {
	private final Player player;
	
	/**
	 * @param data
	 * @throws IOException
	 */
	public ForgeInPacket(final byte[] data, final Player p) throws IOException {
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
