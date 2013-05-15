/**
 * File: ICompressionStreamProvider.java
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
public interface ICompressionStreamProvider {
	public InputStream getInputStream(InputStream in) throws IOException;
	
	public OutputStream getOutputStream(OutputStream out) throws IOException;
	
	public String getName();
}
