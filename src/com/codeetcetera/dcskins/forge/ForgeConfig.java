/**
 * File: ForgeConfig.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.Configuration;

import com.codeetcetera.dcskins.DCSkinsConfig;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeConfig extends DCSkinsConfig {
	private final Configuration config;
	
	/**
	 * 
	 */
	public ForgeConfig(final File f) {
		defaults.put("main.directory", "config/DCSkins/");
		
		config = new Configuration(f);
		config.load();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#getIntProp(java.lang.String)
	 */
	@Override
	public int getIntProp(final String name) {
		return config.get(Configuration.CATEGORY_GENERAL, name,
				(Integer) defaults.get(name)).getInt();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.DCSkinsConfig#getStringProp(java.lang.String)
	 */
	@Override
	public String getStringProp(final String name) {
		return config.get(Configuration.CATEGORY_GENERAL, name,
				(String) defaults.get(name)).getString();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#getBoolProp(java.lang.String)
	 */
	@Override
	public boolean getBoolProp(final String name) {
		return config.get(Configuration.CATEGORY_GENERAL, name,
				(Boolean) defaults.get(name)).getBoolean(false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#getIntProp(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public int getIntProp(final String cat, final String name) {
		if(defaults.get(cat) instanceof HashMap<?, ?>) {
			return config.get(cat, name,
					((HashMap<String, Integer>) defaults.get(cat)).get(name))
							.getInt();
		}
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.DCSkinsConfig#onSave()
	 */
	@Override
	public void onSave() {
		if(config.hasChanged()) {
			config.save();
		}
	}
	
}
