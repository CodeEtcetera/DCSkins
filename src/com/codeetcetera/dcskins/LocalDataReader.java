/**
 * File: LocalDataReader.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.codeetcetera.dcskins.datatypes.DataTypeRegistry;
import com.codeetcetera.dcskins.datatypes.IDataType;

/**
 * @author CodeEtcetera
 * 
 */
public class LocalDataReader {
	private final DataTypeRegistry dataTypeManager;
	
	private final String mainDir;
	
	/**
	 * 
	 */
	public LocalDataReader() {
		mainDir = DCSkinsConfig.getInstance().getStringProp("main.directory");
		dataTypeManager = DataTypeRegistry.getInstance();
	}
	
	/**
	 * Read local data for a user
	 * 
	 * @param user
	 * @param dataType
	 * @return The data read or null if there was no data
	 * @throws IOException
	 */
	public byte[] readLocal(final String user, final byte dataType)
			throws IOException {
		IDataType type = dataTypeManager.getDataType(dataType);
		if(type == null) {
			return null;
		}
		return readLocal(user, dataType, type.getName() + "/");
	}
	
	/**
	 * Read local data for a user
	 * 
	 * @param user
	 *            The user
	 * @param dataType
	 *            The type of the data
	 * @param typeDir
	 *            The directory the file should be located in
	 * @return The data read or null if there was no data
	 * @throws IOException
	 */
	public byte[] readLocal(final String user, final byte dataType,
			final String typeDir) throws IOException {
		File f = new File(mainDir + typeDir + user + ".png");
		if(!f.exists()) {
			return null;
		}
		
		int len = (int) f.length();
		byte[] data = new byte[len];
		
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			in.read(data, 0, len);
		} catch(IOException e) {
			throw e;
		} finally {
			if(in != null) {
				in.close();
			}
		}
		
		return data;
	}
	
	/**
	 * Write local data
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void writeLocal(final String user, final byte dataType,
			final byte[] data) throws IOException {
		IDataType type = dataTypeManager.getDataType(dataType);
		if(type == null) {
			return;
		}
		
		File f = new File(mainDir + type.getName() + "/" + user + ".png");
		if(!f.exists()) {
			f.createNewFile();
		}
		
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(f);
			out.write(data);
		} catch(IOException e) {
			throw e;
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}
}
