/**
 * File: FileKeyCache.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.codeetcetera.dcskins.DCSkinsConfig;
import com.codeetcetera.dcskins.DCSkinsLog;
import com.codeetcetera.dcskins.compression.ICompression;
import com.codeetcetera.dcskins.datatypes.DataTypeRegistry;

/**
 * @author CodeEtcetera
 * 
 */
public class FileKeyCache implements IKeyCache {
	public static final String KEYCACHE_FILE = "cache";
	
	private final DataTypeRegistry dataTypeManager;
	
	private final HashMap<Byte, HashMap<String, String>> keyCache;
	private final HashMap<Byte, HashMap<String, Integer>> keyUses;
	
	private final IKeyGenerator keyGen;
	private final ICompression streamProvider;
	
	private final String cacheDir;
	
	private final String mainDir;
	
	/**
	 * @param keyGen
	 * @throws IOException
	 */
	public FileKeyCache(final ICompression streamProvider,
			final IKeyGenerator keyGen) throws IOException {
		this.keyGen = keyGen;
		this.streamProvider = streamProvider;
		cacheDir =
			DCSkinsConfig.getInstance().getStringProp("cache.subdirectory");
		mainDir = DCSkinsConfig.getInstance().getStringProp("main.directory");
		
		dataTypeManager = DataTypeRegistry.getInstance();
		
		keyCache = new HashMap<Byte, HashMap<String, String>>(4);
		keyUses = new HashMap<Byte, HashMap<String, Integer>>(4);
		
		for(Byte id : dataTypeManager.getIdentifiers()) {
			keyCache.put(id, new HashMap<String, String>());
			keyUses.put(id, new HashMap<String, Integer>());
		}
		
		readCache();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IKeyCache#inCache(java.lang.String,
	 * byte)
	 */
	@Override
	public boolean inCache(final String user, final byte dataType)
			throws IOException {
		HashMap<String, String> map = keyCache.get(dataType);
		
		if(map == null) {
			return false;
		}
		
		return map.containsKey(user);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IKeyCache#insert(java.lang.String,
	 * byte, java.lang.String)
	 */
	@Override
	public boolean insert(final String user, final byte dataType,
			final String key) throws IllegalArgumentException, IOException {
		HashMap<String, String> map = keyCache.get(dataType);
		
		if(map == null || map.containsKey(user)) {
			return false;
		}
		
		map.put(user, key);
		return updateKeyUses(key, dataType, +1);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IKeyCache#update(java.lang.String,
	 * byte, java.lang.String)
	 */
	@Override
	public boolean update(final String user, final byte dataType,
			final String newKey) throws IOException {
		HashMap<String, String> map = keyCache.get(dataType);
		
		if(map == null || !map.containsKey(user)) {
			return false;
		}
		
		// Update old key num uses
		String oldKey = map.get(user);
		if(oldKey.equals(newKey) || !updateKeyUses(oldKey, dataType, -1)) {
			return false;
		}
		
		// Insert new key, this overwrites the old one
		map.put(user, newKey);
		return updateKeyUses(newKey, dataType, +1);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.cache.IKeyCache#removeByUser(java.lang.String,
	 * byte)
	 */
	@Override
	public boolean remove(final String user, final byte dataType)
			throws IOException {
		HashMap<String, String> map = keyCache.get(dataType);
		
		if(map == null) {
			return false;
		}
		
		String key = map.remove(user);
		if(key == null) {
			return false;
		}
		
		return updateKeyUses(key, dataType, -1);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IKeyCache#getKey(java.lang.String,
	 * byte)
	 */
	@Override
	public String getKey(final String user, final byte dataType)
			throws IOException {
		HashMap<String, String> map = keyCache.get(dataType);
		
		if(map == null) {
			return null;
		}
		
		return map.get(user);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.cache.IKeyCache#getKeyUseCount(java.lang.String)
	 */
	@Override
	public int getKeyUseCount(final String key, final byte dataType)
			throws IOException {
		HashMap<String, Integer> map = keyUses.get(dataType);
		
		if(map == null) {
			return -1;
		}
		
		return 0;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IKeyCache#onSave()
	 */
	@Override
	public void onSave() throws IOException {
		writeCache();
	}
	
	private boolean updateKeyUses(final String key, final byte dataType,
			final int diff) {
		HashMap<String, Integer> map = keyUses.get(dataType);
		
		if(map == null) {
			return false;
		}
		
		if(map.containsKey(key)) {
			map.put(key, (map.get(key) + diff));
		} else {
			map.put(key, diff);
		}
		return true;
	}
	
	private void readCache() throws IOException {
		KeyInputStream in = null;
		try {
			File f = new File(mainDir + cacheDir + FileKeyCache.KEYCACHE_FILE);
			in =
				new KeyInputStream(
						streamProvider.getInputStream(new FileInputStream(f)),
						keyGen);
			
			byte[] buf = new byte[keyGen.getKeyLength()];
			int nBlocks = in.readByte();
			DCSkinsLog.debug("Key cache holds " + nBlocks + " blocks");
			for(int i = 0; i < nBlocks; i++) {
				readTypeBlock(in, buf);
			}
		} catch(final EOFException eof) {
			writeCache();
		} catch(final IOException e) {
			throw e;
		} finally {
			if(in != null) {
				in.close();
			}
		}
	}
	
	private void readTypeBlock(final KeyInputStream in, final byte[] buf)
			throws IOException {
		byte dataType = in.readByte();
		
		int nEntries = in.readInt();
		DCSkinsLog.debug("Block " + dataType + " (" + nEntries + ")");
		for(int i = 0; i < nEntries; i++) {
			String user = in.readUTF();
			String key = in.readKey(buf);
			DCSkinsLog.debug(user + "-" + key);
			
			// Insert the key into the map
			insert(user, dataType, key);
		}
	}
	
	/**
	 * Write the in mem key cache to the key cache file
	 * 
	 * @throws IOException
	 */
	public void writeCache() throws IOException {
		DCSkinsLog.debug("Write key cache");
		File f = new File(mainDir + cacheDir + FileKeyCache.KEYCACHE_FILE);
		File backupf =
			new File(mainDir + cacheDir + FileKeyCache.KEYCACHE_FILE + ".bak");
		
		// Remove existing backup
		if(backupf.exists()) {
			backupf.delete();
		}
		// Backup old file
		if(f.exists()) {
			f.renameTo(backupf);
		}
		KeyOutputStream out = null;
		try {
			out =
				new KeyOutputStream(
						streamProvider.getOutputStream(new FileOutputStream(f)),
						keyGen);
			
			out.writeByte(keyCache.size());
			for(Byte id : dataTypeManager.getIdentifiers()) {
				if(keyCache.get(id) != null) {
					writeTypeBlock(out, id);
				}
			}
		} catch(IOException e) {
			// Release the file
			if(out != null) {
				out.flush();
				out.close();
			}
			// Restore previous file
			backupf.renameTo(f);
			throw e;
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
		if(backupf.exists()) {
			backupf.delete();
		}
	}
	
	private void writeTypeBlock(final KeyOutputStream out, final byte type)
			throws IOException {
		out.writeByte(type);
		HashMap<String, String> map = keyCache.get(type);
		out.writeInt(map.size());
		for(Entry<String, String> e : map.entrySet()) {
			out.writeUTF(e.getKey());
			out.writeKey(e.getValue());
		}
	}
}
