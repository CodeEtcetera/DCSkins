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
	 * Get the name of this data type. This name is also used as directory name
	 * for this data type
	 * 
	 * @return The name of this data type
	 */
	public String getName();
	
	/**
	 * Get the unique identifier for this data type
	 * 
	 * @return An unique byte identifier for this data type
	 */
	public byte getDataType();
}
