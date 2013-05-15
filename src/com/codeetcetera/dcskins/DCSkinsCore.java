/**
 * File: DCSkinsCore.java
 * 
 */
package com.codeetcetera.dcskins;

/**
 * @author CodeEtcetera
 * 
 */
public class DCSkinsCore {
	private static DCSkinsCore instance;
	private static CommonClient client;
	
	public static final String MOD_VERSION = "1.2";
	public static final String MC_VERSION = "1.5.1";
	public static final String PACKET_CHANNEL = "DCSkins";
	public static final String LOG_CHANNEL = "DCSkins";
	
	// public static final boolean DEBUG = true;
	
	/**
	 * @return the client proxy
	 */
	public static CommonClient getClient() {
		return DCSkinsCore.client;
	}
	
	/**
	 * @param client
	 *            the client proxy to set
	 */
	public static void setClientProxy(final CommonClient client) {
		DCSkinsCore.client = client;
	}
}
