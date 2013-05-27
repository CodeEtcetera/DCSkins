/**
 * File: DCSkinsConfig.java
 * 
 */
package com.codeetcetera.dcskins;

import java.util.HashMap;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class DCSkinsConfig {
	private static DCSkinsConfig instance;
	
	protected HashMap<String, Object> defaults;
	
	public static DCSkinsConfig getInstance() {
		return DCSkinsConfig.instance;
	}
	
	/**
	 * 
	 */
	public DCSkinsConfig() {
		DCSkinsConfig.instance = this;
		defaults = new HashMap<String, Object>();
		defaults.put("client.waitforservertime", 30000);
		defaults.put("main.debug", true);
		defaults.put("main.offline", true);
		defaults.put("cache.subdirectory", "cache/");
		defaults.put("cache.keycache.class",
				"com.codeetcetera.dcskins.cache.FileKeyCache");
		defaults.put("cache.datacache.class",
				"com.codeetcetera.dcskins.cache.FileDataCache");
		defaults.put("cache.keygen.class",
				"com.codeetcetera.dcskins.cache.SHAKeyGenerator");
		defaults.put("cache.compression.class",
				"com.codeetcetera.dcskins.compression.GzipCompression");
		HashMap<String, Integer> compressions = new HashMap<String, Integer>();
		compressions.put(
				"com.codeetcetera.dcskins.compression.GzipCompression", 1);
		compressions.put("com.codeetcetera.dcskins.compression.NoCompression",
				10);
		defaults.put("compression.preference", compressions);
	}
	
	protected boolean isClientProp(final String name) {
		return name.startsWith("client.");
	}
	
	public abstract int getIntProp(String name);
	
	public abstract int getIntProp(String cat, String name);
	
	public abstract String getStringProp(String name);
	
	public abstract boolean getBoolProp(String name);
	
	public abstract void onSave();
}
