/**
 * File: FileDataCache.java
 * 
 */
package com.codeetcetera.dcskins.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.codeetcetera.dcskins.DCSkinsConfig;
import com.codeetcetera.dcskins.compression.ICompression;
import com.codeetcetera.dcskins.datatypes.DataTypeRegistry;
import com.codeetcetera.dcskins.datatypes.IDataType;

/**
 * @author CodeEtcetera
 * 
 */
public class FileDataCache implements IDataCache {
	private final DataTypeRegistry dataTypeManager;
	
	private final ICompression streamProvider;
	
	private final String cacheDir;
	
	private final String mainDir;
	
	/**
	 * 
	 */
	public FileDataCache(final ICompression streamProvider) {
		this.streamProvider = streamProvider;
		cacheDir =
			DCSkinsConfig.getInstance().getStringProp("cache.subdirectory");
		mainDir = DCSkinsConfig.getInstance().getStringProp("main.directory");
		
		dataTypeManager = DataTypeRegistry.getInstance();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IDataCache#inCache(java.lang.String,
	 * byte)
	 */
	@Override
	public boolean inCache(final String key, final byte dataType) {
		return getCacheFile(key, dataType).exists();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IDataCache#insert(java.lang.String,
	 * byte, byte[])
	 */
	@Override
	public boolean insert(final String key, final byte dataType,
			final byte[] data) throws IOException {
		File f = getCacheFile(key, dataType);
		if(f.exists()) {
			return false;
		}
		
		writeData(f, data);
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IDataCache#update(java.lang.String,
	 * byte, byte[])
	 */
	@Override
	public boolean update(final String oldKey, final boolean deleteOld,
			final String newKey, final byte dataType, final byte[] data)
			throws IOException {
		File f = getCacheFile(oldKey, dataType);
		if(!f.exists() || (deleteOld && !f.delete())) {
			return false;
		}
		
		return insert(newKey, dataType, data);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IDataCache#remove(java.lang.String,
	 * byte)
	 */
	@Override
	public boolean remove(final String key, final byte dataType)
			throws IOException {
		File f = getCacheFile(key, dataType);
		if(!f.exists()) {
			return false;
		}
		
		return f.delete();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IDataCache#getData(java.lang.String,
	 * byte)
	 */
	@Override
	public byte[] getData(final String key, final byte dataType)
			throws IOException {
		File f = getCacheFile(key, dataType);
		
		return readData(f);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.cache.IDataCache#onSave()
	 */
	@Override
	public void onSave() throws IOException {
	}
	
	private byte[] readData(final File f) throws IOException {
		if(!f.exists()) {
			return null;
		}
		
		DataInputStream in = null;
		try {
			in =
				new DataInputStream(
						streamProvider.getInputStream(new FileInputStream(f)));
			int len = in.readInt();
			byte[] data = new byte[len];
			in.readFully(data, 0, len);
			return data;
		} catch(final IOException e) {
			throw e;
		} finally {
			if(in != null) {
				in.close();
			}
		}
	}
	
	private void writeData(final File f, final byte[] data) throws IOException {
		f.getParentFile().mkdirs();
		
		if(f.exists()) {
			f.delete();
		}
		
		DataOutputStream out = null;
		try {
			out =
				new DataOutputStream(
						streamProvider.getOutputStream(new FileOutputStream(f)));
			out.writeInt(data.length);
			out.write(data);
		} catch(IOException e) {
			if(out != null) {
				out.flush();
				out.close();
			}
			f.delete();
			throw e;
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	private File getCacheFile(final String key, final byte type) {
		IDataType dataType = dataTypeManager.getDataType(type);
		if(dataType == null) {
			return null;
		}
		
		return getCacheFile(key, dataType.getName() + "/");
	}
	
	private File getCacheFile(final String key, final String typeDir) {
		return new File(mainDir + cacheDir + typeDir + getSubDir(key) + key
				+ ".dat");
	}
	
	private String getSubDir(final String key) {
		return key.substring(0, 4);
	}
}
