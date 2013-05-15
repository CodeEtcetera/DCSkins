/**
 * File: CapeDataType.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public class CapeDataType implements IDataType {
	public static final byte TYPE_CAPE = 1;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IDataType#getName()
	 */
	@Override
	public String getName() {
		return "Capes";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IDataType#getDataType()
	 */
	@Override
	public byte getDataType() {
		return CapeDataType.TYPE_CAPE;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IDataType#urlIndicators()
	 */
	@Override
	public String[] urlIndicators() {
		return new String[] {"cape", "cloack"};
	}
}
