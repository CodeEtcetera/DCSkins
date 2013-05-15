/**
 * File: DefaultSkinLink.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public class DefaultSkinLink implements IPatternDataLink {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IPatternDataLink#getPattern()
	 */
	@Override
	public String getPattern() {
		return "http://skins.minecraft.net/MinecraftSkins/(.+)\\.png";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IPatternDataLink#getIdentifier()
	 */
	@Override
	public byte getIdentifier() {
		return SkinDataType.TYPE_SKIN;
	}
}
