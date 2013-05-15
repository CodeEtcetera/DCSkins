/**
 * File: ForgeDCSkins.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;
import java.util.Map;

import com.google.common.eventbus.Subscribe;

import com.codeetcetera.dcskins.CommonClient;
import com.codeetcetera.dcskins.CommonServer;
import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.DCSkinsLog;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;

/**
 * @author CodeEtcetera
 * 
 */
// TODO: Correct transformer exclusion
@MCVersion(DCSkinsCore.MC_VERSION)
public class ForgeDCSkins implements IFMLLoadingPlugin {
	private static ForgeDCSkins instance;
	
	private CommonServer proxy;
	
	public static ForgeDCSkins getInstance() {
		return ForgeDCSkins.instance;
	}
	
	/**
	 * 
	 */
	public ForgeDCSkins() {
		ForgeDCSkins.instance = this;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.relauncher.IFMLLoadingPlugin#getLibraryRequestClass()
	 */
	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.relauncher.IFMLLoadingPlugin#getASMTransformerClass()
	 */
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"com.codeetcetera.dcskins.asm."
				+ "ClientClassTransformer"};
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.relauncher.IFMLLoadingPlugin#getModContainerClass()
	 */
	@Override
	public String getModContainerClass() {
		return "com.codeetcetera.dcskins.forge.ForgeModContainer";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.relauncher.IFMLLoadingPlugin#getSetupClass()
	 */
	@Override
	public String getSetupClass() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.relauncher.IFMLLoadingPlugin#injectData(java.util.Map)
	 */
	@Override
	public void injectData(final Map<String, Object> data) {
	}
	
	@Subscribe
	public void preInit(final FMLPreInitializationEvent event) throws Exception {
		new ForgeDCSLog();
		new ForgeConfig(event.getSuggestedConfigurationFile());
	}
	
	@Subscribe
	public void init(final FMLInitializationEvent event) throws Exception {
		// Load the side dependent proxy
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if(side == Side.CLIENT) {
			DCSkinsLog.debug("Client side found");
			proxy = new ForgeClientProxy();
			DCSkinsCore.setClientProxy((CommonClient) proxy);
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
