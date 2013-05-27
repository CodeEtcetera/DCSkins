/**
 * File: ForgeDCSkins.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;

import com.google.common.eventbus.Subscribe;

import com.codeetcetera.dcskins.CommonServer;
import com.codeetcetera.dcskins.DCSkinsLog;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeDCSkins {
	private static ForgeDCSkins instance;
	
	private CommonServer proxy;
	
	public static ForgeDCSkins getInstance() {
		if(instance == null) {
			new ForgeDCSkins();
		}
		
		return instance;
	}
	
	/**
	 * 
	 */
	public ForgeDCSkins() {
		ForgeDCSkins.instance = this;
		
		new ForgeDCSLog();
	}
	
	@Subscribe
	public void preInit(final FMLPreInitializationEvent event) throws Exception {
		new ForgeConfig(event.getSuggestedConfigurationFile());
		DCSkinsLog.info("Log file: %s", event.getSuggestedConfigurationFile()
												.getPath());
	}
	
	@Subscribe
	public void init(final FMLInitializationEvent event) throws Exception {
		// Load the side dependent proxy
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			DCSkinsLog.debug("Client side found");
			proxy = new ForgeClientProxy();
		} else if(side == Side.SERVER) {
			DCSkinsLog.debug("Server side found");
			proxy = new ForgeServerProxy();
		} else if(side == Side.BUKKIT) {
			DCSkinsLog.debug("Bukkit side found");
			throw new Exception(
					"No suport for bukkit through Forge! Use the bukkit plugin");
		}
	}
	
	@Subscribe
	public void postInit(final FMLPostInitializationEvent event)
			throws Exception {
		// Initialize the proxy
		proxy.init();
	}
	
	/**
	 * @param event
	 * @throws IOException
	 */
	@Subscribe
	public void onServerStop(final FMLServerStoppingEvent event)
			throws IOException {
		proxy.onSave();
	}
}
