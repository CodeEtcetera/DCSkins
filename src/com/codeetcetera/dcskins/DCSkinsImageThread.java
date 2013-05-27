/**
 * File: DCSkinsImageThread.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;

import com.google.common.base.Throwables;

import com.codeetcetera.dcskins.datatypes.DataLinkRegistry;
import com.codeetcetera.dcskins.datatypes.DataLinkResult;

/**
 * @author CodeEtcetera
 * 
 */
public class DCSkinsImageThread implements Runnable {
	private final ThreadDownloadImageData data;
	private final String url;
	private final IImageBuffer buffer;
	
	public DCSkinsImageThread(final ThreadDownloadImageData data,
			final String url, final IImageBuffer buffer) {
		this.data = data;
		this.url = url;
		this.buffer = buffer;
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		CommonClient client = CommonClient.getInstance();
		DataLinkRegistry dataLinkManager = DataLinkRegistry.getInstance();
		
		DCSkinsLog.debug("Request for %s", url);
		
		byte[] d = null;
		// Match the url with known url's and retrieve the user and dataType
		DataLinkResult res = dataLinkManager.getLink(url);
		if(res != null) {
			DCSkinsLog.debug("We know the user/type: %s/%d", res.getUser(),
					res.getIdentifier());
			// grab the data from the DCSkins system
			try {
				d = client.getData(res.getUser(), res.getIdentifier());
			} catch(IOException e) {
				DCSkinsLog.warning("Error on client cache retrieval: \n%s",
						Throwables.getStackTraceAsString(e));
			}
		}
		
		if((d == null || d.length == 0)
				&& !DCSkinsConfig.getInstance().getBoolProp("main.offline")) {
			DCSkinsLog.debug("Data could not be retreived from the DCSkins"
					+ " system");
			if(res == null) {
				// We don't know the user or dataType, so we can't cache it
				httpGrab(true);
				return;
			}
			// We do know the user and dataType, save the data in the cache
			d = httpGrab(false);
			
			DCSkinsLog.debug("Put data from http");
			try {
				client.setData(res.getUser(), res.getIdentifier(), d);
			} catch(IOException e) {
				DCSkinsLog.warning("Error on client data cache: \n%s",
						Throwables.getStackTraceAsString(e));
			}
		}
		
		if(d == null || d.length == 0) {
			return;
		}
		
		ByteArrayInputStream stream = new ByteArrayInputStream(d);
		grabImage(stream);
	}
	
	private byte[] httpGrab(final boolean directGrab) {
		// Adapted version of the original HTTP grab
		
		HttpURLConnection conn = null;
		try {
			URL loc = new URL(url);
			conn = (HttpURLConnection) loc.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.connect();
			
			if(conn.getResponseCode() == 200) {
				InputStream in = conn.getInputStream();
				if(directGrab) {
					grabImage(in);
				} else {
					byte[] buf = new byte[in.available()];
					
					in.read(buf);
					
					return buf;
				}
			}
		} catch(IOException e) {
			// Treat the exception as it would be in the original
			e.printStackTrace();
		} finally {
			if(conn != null) {
				conn.disconnect();
			}
		}
		
		return null;
	}
	
	private void grabImage(final InputStream stream) {
		// Turn the stream into an usable image
		try {
			data.image = ImageIO.read(stream);
			if(buffer != null) {
				DCSkinsLog.debug("Image will be parsed as user skin");
				data.image = buffer.parseUserSkin(data.image);
			}
		} catch(IOException e) {
			// Treat the exception as it would be in the original
			e.printStackTrace();
		}
	}
}
