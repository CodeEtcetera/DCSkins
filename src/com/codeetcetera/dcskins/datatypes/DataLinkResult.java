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
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * @return the identifier
	 */
	public byte getIdentifier() {
		return identifier;
	}
}
