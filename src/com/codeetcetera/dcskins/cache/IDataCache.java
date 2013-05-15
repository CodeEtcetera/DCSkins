/**
 * File: IDataCache.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.io.IOException;

/**
 * @author CodeEtcetera
 * 
 */
public interface IDataCache {
	public boolean inCache(String key, byte dataType) throws IOException;
	
	public boolean insert(String key, byte dataType, byte[] data)
			throws IOException;
	
	public boolean update(String oldKey, boolean deleteOld, String newKey,
			byte dataType, byte[] data) throws IOException;
	
	public boolean remove(String key, byte dataType) throws IOException;
	
	public byte[] getData(String key, byte dataType) throws IOException;
	
	public void onSave() throws IOException;
}
