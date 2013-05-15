/**
 * File: SHAKeyGenerator.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author CodeEtcetera
 * 
 */
public class SHAKeyGenerator implements IKeyGenerator {
	private final char[] HEX_CHAR = new char[] {'0', '1', '2', '3', '4', '5',
												'6', '7', '8', '9', 'A', 'B',
												'C', 'D', 'E', 'F'};
	
	private final MessageDigest sha;
	
	/**
	 * Creates a new key generator using SHA-256
	 * 
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public SHAKeyGenerator() throws NoSuchAlgorithmException {
		sha = MessageDigest.getInstance("SHA-256");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.cacher.KeyGenerator#generateKey(com.codeetcetera
	 * .dcskins.cacher.DataHolder)
	 */
	@Override
	public synchronized String generateKey(final byte[] data) {
		sha.reset();
		byte[] digest = sha.digest(data);
		return byteArrayToHex(digest);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cacher.KeyGenerator#getKeyLength()
	 */
	@Override
	public int getKeyLength() {
		return 64;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.api.IKeyGenerator#getName()
	 */
	@Override
	public String getName() {
		return "SHA-256";
	}
	
	private String byteArrayToHex(final byte[] digest) {
		StringBuilder builder = new StringBuilder();
		for(byte b : digest) {
			builder.append(HEX_CHAR[((b & 0xF0) >> 4)]);
			builder.append(HEX_CHAR[(b & 0x0F)]);
		}
		
		return builder.toString();
	}
}
