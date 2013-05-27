/**
 * File: NoCompressionStream.java
 * 
 */
package com.codeetcetera.dcskins.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author CodeEtcetera
 * 
 */
public class NoCompression implements ICompression {
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.api.ICompressionStreamProvider#getInputStream
	 * (java.io.InputStream)
	 */
	@Override
	public InputStream getInputStream(final InputStream in) throws IOException {
		return in;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.api.ICompressionStreamProvider#getOutputStream
	 * (java.io.OutputStream)
	 */
	@Override
	public OutputStream getOutputStream(final OutputStream out)
			throws IOException {
		return out;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.ICompressionStreamProvider#getName()
	 */
	@Override
	public String getName() {
		return "none";
	}
}
