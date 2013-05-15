/**
 * File: IPatternDataLink.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public interface IPatternDataLink {
	/**
	 * @return The regex pattern for this data linker. The regex MUST include a
	 *         capture for the username
	 */
	public String getPattern();
	
	/**
	 * @return The identifier that should be used when this pattern matches
	 */
	public byte getIdentifier();
}
