/**
 * File: BukkitDCSLog.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.util.logging.Level;

import org.bukkit.plugin.PluginLogger;

import com.codeetcetera.dcskins.DCSkinsLog;

/**
 * @author CodeEtcetera
 * 
 */
public class BukkitDCSLog extends DCSkinsLog {
	private final PluginLogger log;
	
	/**
	 * 
	 */
	public BukkitDCSLog() {
		log = new PluginLogger(BukkitDCSkins.getInstance());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.util.DCSkinsLog#log(java.util.logging.Level,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(final Level level, final String format,
			final Object... data) {
		log.log(level, String.format(format, data));
	}
}
