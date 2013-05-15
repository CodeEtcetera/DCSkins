/**
 * File: KeyInputStream.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author CodeEtcetera
 * 
 */
public class KeyInputStream extends DataInputStream {
	private final IKeyGenerator keyGen;
	
	/**
	 * @param in
	 * @param keyGen
	 */
	public KeyInputStream(final InputStream in, final IKeyGenerator keyGen) {
		super(in);
		this.keyGen = keyGen;
	}
	
	public String readKey(final byte[] buffer) throws IOException {
		readFully(buffer, 0, keyGen.getKeyLength());
		return new String(buffer, 0, keyGen.getKeyLength(),
				Charset.forName("UTF-8"));
	}
	
	public String readKey() throws IOException {
		byte[] buffer = new byte[keyGen.getKeyLength()];
		readFully(buffer, 0, keyGen.getKeyLength());
		return new String(buffer, 0, keyGen.getKeyLength(),
				Charset.forName("UTF-8"));
	}
}
