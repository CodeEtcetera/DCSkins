/**
 * File: CompressionRegistry.java
 * 
 */
package com.codeetcetera.dcskins.compression;

import java.util.HashMap;

/**
 * @author CodeEtcetera
 * 
 */
public class CompressionRegistry {
	private static CompressionRegistry instance;
	
	private final HashMap<String, Byte> compressionNames;
	private final ICompressionStreamProvider[] compressions;
	private byte writePtr;
	
	public static CompressionRegistry getInstance() {
		if(CompressionRegistry.instance == null) {
			CompressionRegistry.instance = new CompressionRegistry();
		}
		
		return CompressionRegistry.instance;
	}
	
	/**
	 * 
	 */
	public CompressionRegistry() {
		compressionNames = new HashMap<String, Byte>();
		compressions = new ICompressionStreamProvider[16];
		writePtr = 0;
	}
	
	/**
	 * @param compression
	 */
	public void register(final ICompressionStreamProvider compression)
			throws Exception {
		if(compressionNames.containsKey(compression.getName())) {
			throw new IllegalArgumentException(
					"The compression provider with name "
							+ compression.getName() + " is already registered");
		}
		
		if(writePtr == 16) {
			throw new Exception(
					"Compression registery has no more space for new"
							+ " compressions");
		}
		
		if(writePtr == 0 && !(compression instanceof NoCompressionStream)) {
			throw new Exception(
					"First compression type should always be no compression");
		}
		
		compressionNames.put(compression.getName(), writePtr);
		compressions[writePtr++] = compression;
	}
	
	/**
	 * Swap to compression providers
	 * 
	 * @param idx1
	 * @param idx2
	 */
	public void swap(final byte idx1, final byte idx2) {
		if(idx1 == idx2) {
			return;
		}
		ICompressionStreamProvider tmp = compressions[idx1];
		compressions[idx1] = compressions[idx2];
		compressions[idx2] = tmp;
		
		compressionNames.put(compressions[idx2].getName(), idx2);
		compressionNames.put(compressions[idx1].getName(), idx1);
	}
	
	/**
	 * @param name
	 * @return The index of this compression or -1 if the compression does not
	 *         exists
	 */
	public byte getCompressionIdx(final String name) {
		Byte idx = compressionNames.get(name);
		if(idx == null) {
			return -1;
		}
		return idx;
	}
	
	/**
	 * @param name
	 *            The name of the key generator
	 * @return The compression provider or null if there is none with that name
	 */
	public ICompressionStreamProvider getCompression(final String name) {
		Byte idx = compressionNames.get(name);
		if(idx == null) {
			return null;
		}
		return getCompression(idx);
	}
	
	/**
	 * @param id
	 *            The id of the key generator
	 * @return The compression provider or null if there is none with that id
	 */
	public ICompressionStreamProvider getCompression(final byte id) {
		if(id < 0 || id > 15) {
			throw new IllegalArgumentException(
					"Compression id is out of range (0-15)");
		}
		
		return compressions[id];
	}
	
	/**
	 * @return The number of registered compression providers
	 */
	public int getNumCompressions() {
		return writePtr;
	}
}
