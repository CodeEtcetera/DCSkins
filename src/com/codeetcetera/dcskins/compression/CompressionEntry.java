/**
 * File: CompressionEntry.java
 * 
 */
package com.codeetcetera.dcskins.compression;

/**
 * @author CodeEtcetera
 * 
 */
public class CompressionEntry implements Comparable<CompressionEntry> {
	private final byte localIdx;
	private byte remoteIdx;
	private final int preference;
	private final ICompression compression;
	
	public CompressionEntry(final byte localIdx, final int preference,
			final ICompression compression) {
		super();
		this.compression = compression;
		this.localIdx = localIdx;
		this.preference = preference;
		remoteIdx = -1;
	}
	
	/**
	 * @return The remote index or -1 if it is not set yet
	 */
	public byte getRemoteIdx() {
		return remoteIdx;
	}
	
	/**
	 * Set the remote index
	 * 
	 * @param remoteIdx
	 */
	public void setRemoteIdx(final byte remoteIdx) {
		this.remoteIdx = remoteIdx;
	}
	
	/**
	 * @return The local index
	 */
	public byte getLocalIdx() {
		return localIdx;
	}
	
	/**
	 * @return The preference
	 */
	public int getPreference() {
		return preference;
	}
	
	/**
	 * @return The compression
	 */
	public ICompression getCompression() {
		return compression;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final CompressionEntry other) {
		if(other.getPreference() < preference) {
			// Put me higher on the list since i have a higher preference
			// Note: higher means less likely to be picked
			return 1;
		}
		
		return -1;
	}
}
