/**
 * File: DataLinkManager.java
 * 
 */
package com.codeetcetera.dcskins.datatypes;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CodeEtcetera
 * 
 */
public class DataLinkRegistry {
	private static DataLinkRegistry instance;
	
	private final DataTypeRegistry dataTypeManager;
	private final LinkedList<DataLinkEntry> links;
	
	/**
	 * @return The global instance of the DataLinkRegistry
	 */
	public static DataLinkRegistry getInstance() {
		if(DataLinkRegistry.instance == null) {
			DataLinkRegistry.instance = new DataLinkRegistry();
		}
		
		return DataLinkRegistry.instance;
	}
	
	private DataLinkRegistry() {
		dataTypeManager = DataTypeRegistry.getInstance();
		
		links = new LinkedList<DataLinkEntry>();
	}
	
	/**
	 * Register a new link between an url pattern and a data type
	 * 
	 * @param urlPattern
	 *            The pattern for the url. This pattern should contain a
	 *            capturing group for the username. For syntax see
	 *            java.util.regex.Pattern
	 * @param dataType
	 *            The data type this url should match to. Default data type's
	 *            are SkinDataType.TYPE_SKIN and CapeDataType.TYPE_CAPE
	 */
	public void register(final String urlPattern, final byte dataType) {
		if(dataType < 0 || dataType > 15) {
			throw new IllegalArgumentException(
					"Data link has an dataType outside the 0-15 range");
		}
		
		if(dataTypeManager.getDataType(dataType) == null) {
			throw new IllegalArgumentException(
					"Data link dataType is not known");
		}
		
		Pattern pat = Pattern.compile(urlPattern);
		DataLinkEntry entry = new DataLinkEntry(pat, dataType);
		links.add(entry);
	}
	
	/**
	 * @return The link result which matched the url or null if none matched
	 */
	public DataLinkResult getLink(final String url) {
		Matcher m;
		DataLinkResult result = null;
		for(DataLinkEntry e : links) {
			m = e.getPattern().matcher(url);
			if(m.matches() && m.groupCount() > 0) {
				result = new DataLinkResult(m.group(1), e.getIdentifier());
				break;
			}
		}
		
		return result;
	}
	
	class DataLinkEntry {
		private final Pattern pattern;
		private final byte identifier;
		
		/**
		 * @param pattern
		 * @param identifier
		 */
		public DataLinkEntry(final Pattern pattern, final byte identifier) {
			this.pattern = pattern;
			this.identifier = identifier;
		}
		
		/**
		 * @return The pattern
		 */
		public Pattern getPattern() {
			return pattern;
		}
		
		/**
		 * @return The identifier
		 */
		public byte getIdentifier() {
			return identifier;
		}
	}
}
