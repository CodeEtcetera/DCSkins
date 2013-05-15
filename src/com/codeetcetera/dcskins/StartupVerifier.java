/**
 * File: StartupVerifier.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.File;
import java.io.IOException;

import com.codeetcetera.dcskins.cache.FileKeyCache;
import com.codeetcetera.dcskins.datatypes.DataTypeRegistry;
import com.codeetcetera.dcskins.datatypes.IDataType;

/**
 * @author CodeEtcetera
 * 
 */
public class StartupVerifier {
	private final DataTypeRegistry dataTypeManager;
	private final String mainDir;
	
	/**
	 * 
	 */
	public StartupVerifier() {
		mainDir = DCSkinsConfig.getInstance().getStringProp("main.directory");
		dataTypeManager = DataTypeRegistry.getInstance();
	}
	
	public void verifyGlobal() throws IOException {
		// Verify / create the home directory
		File globalDir = new File(mainDir);
		if(!globalDir.exists() && !globalDir.mkdirs()) {
			throw new IOException("Could not create the home directory "
					+ mainDir);
		}
	}
	
	public void verifyCache() throws IOException {
		final String cacheDir =
			DCSkinsConfig.getInstance().getStringProp("cache.subdirectory");
		// Verify / create the cache directory
		File cacheDirFile = new File(mainDir + cacheDir);
		if(!cacheDirFile.exists() && !cacheDirFile.mkdirs()) {
			throw new IOException("Could not create the cache directory "
					+ mainDir + cacheDir);
		}
		
		// Verify / create the key cache file
		File keyCacheFile =
			new File(mainDir + cacheDir + FileKeyCache.KEYCACHE_FILE);
		if(!keyCacheFile.exists() && !keyCacheFile.createNewFile()) {
			throw new IOException("Could not create the key cache file "
					+ mainDir + cacheDir + FileKeyCache.KEYCACHE_FILE);
		}
		
		// Verify / create the data directory in the cache and local for each
		// data type
		File cacheDataDirFile;
		File dataDirFile;
		IDataType dataType;
		for(Byte id : dataTypeManager.getIdentifiers()) {
			dataType = dataTypeManager.getDataType(id);
			
			dataDirFile = new File(mainDir + dataType.getName() + "/");
			cacheDataDirFile =
				new File(mainDir + cacheDir + dataType.getName() + "/");
			
			if(!dataDirFile.exists() && !dataDirFile.mkdirs()) {
				throw new IOException("Could not create the data directory "
						+ mainDir + dataType.getName() + "/");
			}
			
			if(!cacheDataDirFile.exists() && !cacheDataDirFile.mkdirs()) {
				throw new IOException(
						"Could not create the cache data directory " + mainDir
								+ cacheDir + dataType.getName() + "/");
			}
		}
	}
}
