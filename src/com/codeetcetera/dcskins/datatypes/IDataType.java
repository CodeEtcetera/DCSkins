/**
 * File: IDataType.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public interface IDataType {
	/**
	 * @return The name of this data type
	 */
	public String getName();
	
	/**
	 * @return An array of indicators. If the indicator is in an url it probably
	 *         will be this data type.
	 */
	public String[] urlIndicators();
	
	/**
	 * @return An unique byte identifier for this data type
	 */
	public byte getDataType();
}
