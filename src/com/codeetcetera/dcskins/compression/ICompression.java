/**
 * File: ICompression.java
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
public interface ICompression {
	/**
	 * Transform an inputstream to be decompressed
	 * 
	 * @param in
	 *            The compressed inputstream
	 * @return The decompressed inputstream
	 * @throws IOException
	 */
	public InputStream getInputStream(InputStream in) throws IOException;
	
	/**
	 * Transform an outputstream to be decompressed
	 * 
	 * @param out
	 *            The outputstream to be compressed
	 * @return The compressed outputstream
	 * @throws IOException
	 */
	public OutputStream getOutputStream(OutputStream out) throws IOException;
	
	/**
	 * @return The name of this compression
	 */
	public String getName();
}
