/**
 * File: ForgeDCSLog.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.util.logging.Level;

import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.DCSkinsLog;

import cpw.mods.fml.common.FMLLog;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeDCSLog extends DCSkinsLog {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.codeetcetera.dcskins.util.DCSkinsLog#log(java.util.logging.Level,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(final Level level, final String format,
			final Object... data) {
		FMLLog.log(DCSkinsCore.LOG_CHANNEL, level, format, data);
	}
}
