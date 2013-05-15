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
	
	private final Pattern defaultPattern;
	
	public static DataLinkRegistry getInstance() {
		if(DataLinkRegistry.instance == null) {
			DataLinkRegistry.instance = new DataLinkRegistry();
		}
		
		return DataLinkRegistry.instance;
	}
	
	/**
	 * 
	 */
	public DataLinkRegistry() {
		dataTypeManager = DataTypeRegistry.getInstance();
		
		links = new LinkedList<DataLinkEntry>();
		
		defaultPattern = Pattern.compile(".+/(.+)\\.png");
	}
	
	/**
	 * @param link
	 * @throws Exception
	 */
	public void register(final IPatternDataLink link) {
		byte idx = link.getIdentifier();
		
		if(idx < 0 || idx > 15) {
			throw new IllegalArgumentException(
					"Data link has an indentifier outside the 0-15 range");
		}
		
		if(dataTypeManager.getDataType(idx) == null) {
			throw new IllegalArgumentException(
					"Data link identifier is not known");
		}
		
		Pattern pat = Pattern.compile(link.getPattern());
		DataLinkEntry entry = new DataLinkEntry(pat, idx);
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
		
		// No link found, try the default user extractor & type indicators
		if(result == null) {
			m = defaultPattern.matcher(url);
			if(m.matches()) {
				for(Byte id : dataTypeManager.getIdentifiers()) {
					for(String word : dataTypeManager.getDataType(id)
														.urlIndicators()) {
						if(url.indexOf(word) >= 0) {
							result = new DataLinkResult(m.group(1), id);
						}
					}
				}
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
		 * @return the pattern
		 */
		public Pattern getPattern() {
			return pattern;
		}
		
		/**
		 * @return the identifier
		 */
		public byte getIdentifier() {
			return identifier;
		}
	}
}
