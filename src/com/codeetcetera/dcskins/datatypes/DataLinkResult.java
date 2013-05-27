/**
 * File: DataLinkResult.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

/**
 * @author CodeEtcetera
 * 
 */
public class DataLinkResult {
	private final String user;
	private final byte identifier;
	
	/**
	 * @param user
	 * @param identifier
	 */
	public DataLinkResult(final String user, final byte identifier) {
		super();
		this.user = user;
		this.identifier = identifier;
	}
	
	/**
	 * Get the user the linking resulted in
	 * 
	 * @return The user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Get the data type the linking resulted in
	 * 
	 * @return The identifier
	 */
	public byte getIdentifier() {
		return identifier;
	}
}
