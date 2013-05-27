/**
 * File: BukkitConfig.java
 * 
 */
package com.codeetcetera.dcskins.bukkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

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
		
		Iterator<Entry<String, Object>> i = defaults.entrySet().iterator();
		while(i.hasNext()) {
			Entry<String, Object> e = i.next();
			if(!isClientProp(e.getKey())) {
				if(!core.getConfig().contains(e.getKey())) {
					if(e.getValue() instanceof HashMap<?, ?>) {
						// Create the new section
						ConfigurationSection sect =
							core.getConfig().createSection(e.getKey());
						
						Iterator<Entry<String, Object>> iSub =
							((HashMap<String, Object>) e.getValue()).entrySet()
																	.iterator();
						// Loop over all the items of the new section and insert
						// them
						while(iSub.hasNext()) {
							Entry<String, Object> eSub = iSub.next();
							sect.set(eSub.getKey(), e.getValue());
						}
					} else {
						core.getConfig().set(e.getKey(), e.getValue());
					}
					changed = true;
					
				}
			}
		}
		
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
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#getIntProp(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int getIntProp(final String cat, final String name) {
		ConfigurationSection sect =
			core.getConfig().getConfigurationSection(name);
		if(sect == null) {
			return 0;
		}
		
		return sect.getInt(name);
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
