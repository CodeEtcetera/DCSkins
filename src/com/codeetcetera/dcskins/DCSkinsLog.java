/**
 * File: DCSkinsLog.java
 * 
 */

package com.codeetcetera.dcskins;

import java.util.logging.Level;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class DCSkinsLog {
	protected static DCSkinsLog instance;
	
	public DCSkinsLog() {
		DCSkinsLog.instance = this;
	}
	
	public static void severe(final String format, final Object... data) {
		DCSkinsLog.instance.log(Level.SEVERE, format, data);
	}
	
	public static void warning(final String format, final Object... data) {
		DCSkinsLog.instance.log(Level.WARNING, format, data);
	}
	
	public static void info(final String format, final Object... data) {
		DCSkinsLog.instance.log(Level.INFO, format, data);
	}
	
	public static void debug(final String format, final Object... data) {
		if(DCSkinsConfig.getInstance().getBoolProp("main.debug")) {
			DCSkinsLog.instance.log(Level.INFO, format, data);
		}
	}
	
	public static void fine(final String format, final Object... data) {
		DCSkinsLog.instance.log(Level.FINE, format, data);
	}
	
	public static void finer(final String format, final Object... data) {
		DCSkinsLog.instance.log(Level.FINER, format, data);
	}
	
	public static void finest(final String format, final Object... data) {
		DCSkinsLog.instance.log(Level.FINEST, format, data);
	}
	
	public abstract void log(Level level, String format, Object... data);
}
