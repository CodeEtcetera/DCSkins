/**
 * File: DefaultCapeLink.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public class DefaultCapeLink implements IPatternDataLink {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IPatternDataLink#getPattern()
	 */
	@Override
	public String getPattern() {
		return "http://skins.minecraft.net/MinecraftCloaks/(.+)\\.png";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IPatternDataLink#getIdentifier()
	 */
	@Override
	public byte getIdentifier() {
		return CapeDataType.TYPE_CAPE;
	}
}
