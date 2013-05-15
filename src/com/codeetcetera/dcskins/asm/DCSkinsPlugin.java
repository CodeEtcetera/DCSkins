/**
 * File: ForgeDCSkins.java
 * 
 */
package com.codeetcetera.dcskins.asm;

import java.util.Map;

import com.codeetcetera.dcskins.DCSkinsCore;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

/**
 * @author CodeEtcetera
 * 
 */
@MCVersion(DCSkinsCore.MC_VERSION)
@TransformerExclusions({"com.codeetcetera.dcskins.asm"})
public class DCSkinsPlugin implements IFMLLoadingPlugin {
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
}
