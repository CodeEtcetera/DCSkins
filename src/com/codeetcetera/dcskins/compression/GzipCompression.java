/**
 * File: GZipCompressionStream.java
 * 
 */
package com.codeetcetera.dcskins.compression;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author CodeEtcetera
 * 
 */
public class GzipCompression implements ICompression {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.api.ICompressionStreamProvider#getInputStream
	 * (java.io.InputStream)
	 */
	@Override
	public InputStream getInputStream(final InputStream in) throws IOException {
		return new GZIPInputStream(in);
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
		return new GZIPOutputStream(out);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.ICompressionStreamProvider#getName()
	 */
	@Override
	public String getName() {
		return "Gzip";
	}
}
