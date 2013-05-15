/**
 * File: DataTypeManager.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

import java.util.ArrayList;

/**
 * @author CodeEtcetera
 * 
 */
public class DataTypeRegistry {
	private static DataTypeRegistry instance;
	
	private final IDataType[] dataTypes;
	private final ArrayList<Byte> dataTypeIdentifiers;
	
	/**
	 * @return The global instance of DataTypeManager
	 */
	public static DataTypeRegistry getInstance() {
		if(DataTypeRegistry.instance == null) {
			DataTypeRegistry.instance = new DataTypeRegistry();
		}
		return DataTypeRegistry.instance;
	}
	
	private DataTypeRegistry() {
		dataTypes = new IDataType[16];
		dataTypeIdentifiers = new ArrayList<Byte>(2);
	}
	
	/**
	 * Register a new data type to this manager
	 * 
	 * @param dataType
	 *            The new data type
	 * @throws Exception
	 *             If the identifier was already taken
	 */
	public void register(final IDataType dataType) throws Exception {
		byte idx = dataType.getDataType();
		if(idx < 0 || idx > 15) {
			throw new IllegalArgumentException(
					"Data type has an indentifier outside the 0-15 range");
		}
		
		if(dataTypes[idx] != null) {
			throw new Exception("Try to register data type "
					+ dataType.getName() + " to " + idx
					+ " which is already taken by " + dataTypes[idx].getName());
		}
		
		dataTypes[idx] = dataType;
		dataTypeIdentifiers.add(idx);
	}
	
	/**
	 * @return The list of registered identifiers
	 */
	public ArrayList<Byte> getIdentifiers() {
		return dataTypeIdentifiers;
	}
	
	/**
	 * @param identifier
	 * @return The data type using this identifier or null if none use this
	 *         identifier
	 */
	public IDataType getDataType(final byte identifier) {
		return dataTypes[identifier];
	}
}
