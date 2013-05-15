/**
 * File: IKeyGenerator.java
 * 
 */
package com.codeetcetera.dcskins.cache;

/**
 * @author CodeEtcetera
 * 
 */
public interface IKeyGenerator {
	/**
	 * @param data
	 *            The data the key should be created for
	 * @return The generated key
	 */
	public String generateKey(byte[] data);
	
	/**
	 * @return The length of the key in bytes
	 */
	public int getKeyLength();
	
	/**
	 * @return The name of this generator
	 */
	public String getName();
}
