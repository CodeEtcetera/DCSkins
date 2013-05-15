/**
 * File: KeyOutputStream.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author CodeEtcetera
 * 
 */
public class KeyOutputStream extends DataOutputStream {
	private final IKeyGenerator keyGen;
	
	/**
	 * @param out
	 */
	public KeyOutputStream(final OutputStream out, final IKeyGenerator keyGen) {
		super(out);
		this.keyGen = keyGen;
	}
	
	public void writeKey(final String key) throws IOException {
		byte[] b = key.getBytes(Charset.forName("UTF-8"));
		write(b, 0, keyGen.getKeyLength());
	}
}
