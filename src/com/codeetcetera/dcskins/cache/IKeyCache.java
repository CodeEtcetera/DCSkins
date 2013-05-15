/**
 * File: IKeyCache.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public interface IKeyCache {
	public boolean inCache(String user, byte dataType) throws IOException;
	
	public boolean insert(String user, byte dataType, String key)
			throws IllegalArgumentException, IOException;
	
	public boolean update(String user, byte dataType, String newKey)
			throws IOException;
	
	public boolean remove(String user, byte dataType) throws IOException;
	
	public String getKey(String user, byte dataType) throws IOException;
	
	public int getKeyUseCount(String key, byte dataType) throws IOException;
	
	public void onSave() throws IOException;
}
