/**
 * File: CompressionRegistry.java
 * 
 */
package com.codeetcetera.dcskins.compression;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import com.codeetcetera.dcskins.DCSkinsConfig;
import com.codeetcetera.dcskins.DCSkinsLog;

/**
 * @author CodeEtcetera
 * 
 */
public class CompressionRegistry {
	private static CompressionRegistry instance;
	
	private final DCSkinsConfig config;
	
	private final HashMap<String, CompressionEntry> compressionNames;
	private final CompressionEntry[] localCompressions;
	private CompressionEntry[] remoteCompressions;
	private LinkedList<CompressionEntry> prefList;
	
	private byte writePtr;
	
	/**
	 * @return The global instance of the CompressionRegistry
	 */
	public static CompressionRegistry getInstance() {
		if(CompressionRegistry.instance == null) {
			CompressionRegistry.instance = new CompressionRegistry();
		}
		
		return CompressionRegistry.instance;
	}
	
	private CompressionRegistry() {
		config = DCSkinsConfig.getInstance();
		compressionNames = new HashMap<String, CompressionEntry>(5);
		localCompressions = new CompressionEntry[16];
		writePtr = 0;
	}
	
	/**
	 * Reset all the remote indices of the compressions. This is called when the
	 * client loses it's connection
	 */
	public void resetRemote() {
		prefList = null;
		remoteCompressions = null;
		for(byte i = 0; i < 16; i++) {
			if(localCompressions[i] != null) {
				localCompressions[i].setRemoteIdx((byte) -1);
			}
		}
	}
	
	/**
	 * Add a remote compression entry from it's compression's name.
	 * 
	 * @see com.codeetcetera.dcskins.compression.CompressionRegistry#addRemote(CompressionEntry,
	 *      byte)
	 * @param name
	 *            The name of the compression
	 * @param remoteIdx
	 *            The idx the remote end knows this compression under
	 */
	public void addRemote(final String name, final byte remoteIdx) {
		addRemote(getCompression(name), remoteIdx);
	}
	
	/**
	 * Add a remote compression entry. The is called when the client knows the
	 * remote end supports a compression (on DSC response)
	 * 
	 * @param compression
	 *            The compression entry. This has to be from a registered
	 *            compression
	 * @param remoteIdx
	 *            The idx the remote end knows this compression under
	 */
	public void addRemote(final CompressionEntry compression,
			final byte remoteIdx) {
		checkIdxRange(remoteIdx);
		
		if(prefList == null) {
			prefList = new LinkedList<CompressionEntry>();
		}
		if(remoteCompressions == null) {
			remoteCompressions = new CompressionEntry[16];
		}
		
		remoteCompressions[remoteIdx] = compression;
		compression.setRemoteIdx(remoteIdx);
		prefList.add(compression);
	}
	
	/**
	 * Get the preferred compression entry of all the added remote compression
	 * entry's
	 * 
	 * @return The preferred compression entry
	 */
	public CompressionEntry getPreferencedCompression() {
		if(prefList == null) {
			return null;
		}
		Collections.sort(prefList);
		return prefList.getFirst();
	}
	
	/**
	 * Get a compression entry by it's remote id
	 * 
	 * @param idx
	 * @return The compression entry or null if the compression does not exists
	 */
	public CompressionEntry getCompressionRemote(final byte idx) {
		checkIdxRange(idx);
		
		if(remoteCompressions == null || remoteCompressions[idx] == null) {
			return null;
		}
		
		return remoteCompressions[idx];
	}
	
	/**
	 * Get a compression entry by it's local id
	 * 
	 * @param idx
	 * @return The compression entry or null if the compression does not exists
	 */
	public CompressionEntry getCompressionLocal(final byte idx) {
		checkIdxRange(idx);
		
		return localCompressions[idx];
	}
	
	private void checkIdxRange(final byte idx) {
		if(idx < 0 || idx > 15) {
			throw new IllegalArgumentException(
					"Compression id is out of range (0-15): " + idx);
		}
	}
	
	/**
	 * Register a new compression
	 * 
	 * @param The
	 *            compression
	 * @throws Exception
	 *             If the adding of the compression failed
	 */
	public synchronized void register(final ICompression compression)
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
		
		if(writePtr == 0 && !(compression instanceof NoCompression)) {
			throw new Exception(
					"First compression type should always be no compression");
		}
		
		int pref =
			config.getIntProp("compression.preference",
					compression.getClass().getCanonicalName());
		
		DCSkinsLog.debug("Add compression %s at %d, pref %d",
				compression.getName(), writePtr, pref);
		
		// Force no compression to be available
		if(compression instanceof NoCompression && pref == 0) {
			pref = 10;
		}
		
		if(pref > 0) {
			CompressionEntry entry =
				new CompressionEntry(writePtr, pref, compression);
			compressionNames.put(compression.getName(), entry);
			localCompressions[writePtr++] = entry;
		}
	}
	
	/**
	 * Get a compression entry by its compression name
	 * 
	 * @param name
	 * @return The compression entry or null if the compression does not
	 *         exists
	 */
	public CompressionEntry getCompression(final String name) {
		return compressionNames.get(name);
	}
	
	/**
	 * @return The number of registered compression providers
	 */
	public int getNumCompressions() {
		return writePtr;
	}
}
