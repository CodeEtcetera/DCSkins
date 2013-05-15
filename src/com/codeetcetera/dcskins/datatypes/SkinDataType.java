/**
 * File: SkinDataType.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public class SkinDataType implements IDataType {
	public static final byte TYPE_SKIN = 0;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IDataType#getName()
	 */
	@Override
	public String getName() {
		return "Skins";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IDataType#getDataType()
	 */
	@Override
	public byte getDataType() {
		return SkinDataType.TYPE_SKIN;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IDataType#urlIndicators()
	 */
	@Override
	public String[] urlIndicators() {
		return new String[] {"skin"};
	}
}
