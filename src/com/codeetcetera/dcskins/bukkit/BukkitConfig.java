/**
 * File: BukkitConfig.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.configuration.MemoryConfiguration;

import com.codeetcetera.dcskins.DCSkinsConfig;

/**
 * @author CodeEtcetera
 * 
 */
public final class BukkitConfig extends DCSkinsConfig {
	private final BukkitDCSkins core;
	
	/**
	 * 
	 */
	public BukkitConfig() {
		core = BukkitDCSkins.getInstance();
		core.saveDefaultConfig();
		
		defaults.put("main.directory", "plugins/DCSkins/");
		
		boolean changed = false;
		MemoryConfiguration mconfig = new MemoryConfiguration();
		
		Iterator<Entry<String, Object>> i = defaults.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, Object> e = i.next();
			if(!isClientProp(e.getKey())) {
				mconfig.set(e.getKey(), e.getValue());
				
				if(!core.getConfig().contains(e.getKey())) {
					core.getConfig().set(e.getKey(), e.getValue());
					changed = true;
				}
			}
		}
		
		core.getConfig().setDefaults(mconfig);
		if(changed) {
			core.saveConfig();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#getIntProp(java.lang.String,
	 * int)
	 */
	@Override
	public int getIntProp(final String name) {
		return core.getConfig().getInt(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.DCSkinsConfig#getStringProp(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getStringProp(final String name) {
		return core.getConfig().getString(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#getBoolProp(java.lang.String)
	 */
	@Override
	public boolean getBoolProp(final String name) {
		return core.getConfig().getBoolean(name);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#onSave()
	 */
	@Override
	public void onSave() {
	}
}
