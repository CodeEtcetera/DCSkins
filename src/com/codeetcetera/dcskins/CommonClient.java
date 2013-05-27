/**
 * File: CommonClient.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;

import com.codeetcetera.dcskins.compression.CompressionEntry;
import com.codeetcetera.dcskins.compression.CompressionRegistry;
import com.codeetcetera.dcskins.datatypes.DataLinkRegistry;
import com.codeetcetera.dcskins.datatypes.SkinDataType;
import com.codeetcetera.dcskins.network.AbstractInPacket;
import com.codeetcetera.dcskins.network.DiscoveryPacket;
import com.codeetcetera.dcskins.network.Packet;
import com.codeetcetera.dcskins.network.PushPacket;
import com.codeetcetera.dcskins.network.Request;
import com.codeetcetera.dcskins.network.RequestPacket;
import com.codeetcetera.dcskins.network.VerifyPacket;

/**
 * @author CodeEtcetera
 * 
 */
public class CommonClient extends CommonServer {
	private static CommonClient instance;
	
	protected String user;
	protected byte[][] locals;
	protected List<Request> requests;
	protected boolean contactServer;
	protected CompressionEntry compressionUsed;
	
	private final int timeout;
	private boolean handshakeDone;
	
	public static CommonClient getInstance() {
		return instance;
	}
	
	/**
	 * @throws Exception
	 */
	public CommonClient() throws Exception {
		CommonClient.instance = this;
		
		user = null;
		locals = new byte[16][];
		requests = Collections.synchronizedList(new LinkedList<Request>());
		contactServer = false;
		
		handshakeDone = false;
		
		timeout =
			DCSkinsConfig.getInstance().getIntProp("client.waitforservertime");
		
		DataLinkRegistry linkReg = DataLinkRegistry.getInstance();
		linkReg.register(
				"http://skins.minecraft.net/MinecraftSkins/(.+)\\.png",
				SkinDataType.TYPE_SKIN);
		linkReg.register(
				"http://skins.minecraft.net/MinecraftCloaks/(.+)\\.png",
				SkinDataType.TYPE_SKIN);
		
		DCSkinsLog.debug("CommonClient Constructed");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.codeetcetera.dcskins.CommonServer#init()
	 */
	@Override
	public void init() throws Exception {
		super.init();
		
		user = Minecraft.getMinecraft().session.username;
		DCSkinsLog.debug("USER: %s", user);
		
		// Load the local data
		for(Byte id : dataTypeRegistry.getIdentifiers()) {
			locals[id] = localDataReader.readLocal(user, id);
			if(locals[id] != null) {
				DCSkinsLog.debug("Read local data for type %d", id);
				// Update local cache
				updateCache(user, id, locals[id]);
			}
		}
		
		// Save local cache
		onSave();
	}
	
	/**
	 * @param user
	 * @throws IOException
	 */
	public void onLogin() throws IOException {
		// Send a DSC to the server
		Request req = new Request("", (byte) 0);
		insertRequest(req);
		packetSender.sendToServer(new DiscoveryPacket());
		DCSkinsLog.debug("DSC send!");
	}
	
	/**
	 * @throws IOException
	 */
	public void onLogout() throws IOException {
		DCSkinsLog.debug("Logout!");
		handshakeDone = false;
		CompressionRegistry.getInstance().resetRemote();
		requests.clear();
		contactServer = false;
		onSave();
	}
	
	/**
	 * @param user
	 * @param type
	 * @return The data for this user for this type. This can return a empty
	 *         byte array if no data was found
	 */
	public byte[] getData(final String user, final byte dataType)
			throws IOException {
		DCSkinsLog.debug("Client grabbing %d for %s", dataType, user);
		// Client logged in event not triggered yet, wait for it to finish
		while(!handshakeDone) {
			synchronized(this) {
				try {
					// Double check
					if(!handshakeDone) {
						wait();
					}
				} catch(InterruptedException ignored) {
				}
			}
		}
		DCSkinsLog.debug("Client is logged in, handle request...");
		
		byte[] data = null;
		
		if(this.user.equals(user)) {
			DCSkinsLog.debug("Try grabbing local user data");
			// Get local data for local players
			data = locals[dataType];
		} else {
			DCSkinsLog.debug("Try grabbing local cache data");
			// Get data from local cache (VERIFY)
			data = getLocalCacheData(user, dataType);
		}
		
		if((data == null || data.length == 0) && contactServer) {
			DCSkinsLog.debug("Try grabbing server data");
			// Get data from server (REQUEST)
			data = getServerCacheData(user, dataType);
		}
		
		if(data == null || data.length == 0) {
			DCSkinsLog.debug("Try grabbing default data");
			// Get default data
			data = defaults[dataType];
		}
		
		if(data == null || data.length == 0) {
			DCSkinsLog.debug("No data available!");
			// No data available
		}
		
		return data;
	}
	
	/**
	 * @param user
	 * @param dataType
	 * @param data
	 * @throws IOException
	 */
	public void setData(final String user, final byte dataType,
			final byte[] data) throws IOException {
		String newKey = keyGen.generateKey(data);
		
		if(keyCache.inCache(user, dataType)) {
			String key = keyCache.getKey(user, dataType);
			if(keyCache.update(user, dataType, newKey)) {
				dataCache.update(key,
						(keyCache.getKeyUseCount(key, dataType) == 0), newKey,
						dataType, data);
			}
		} else {
			if(keyCache.insert(user, dataType, newKey)) {
				dataCache.insert(newKey, dataType, data);
			}
		}
		
		if(contactServer) {
			pushLocalData(dataType, data);
		}
	}
	
	@Override
	protected void handlePacket(final byte dataType, final String user,
			final AbstractInPacket packet) throws IOException {
		if(packet.getPacketType() != Packet.PACKETTYPE_RSP) {
			super.handlePacket(dataType, user, packet);
		} else {
			// Get intial request
			Request request = getRequest(user, dataType);
			if(request == null) {
				return;
			}
			
			DCSkinsLog.debug("Handle response for request %s", request);
			
			request.setResponseType(packet.getStream().readByte());
			
			handleResponsePacket(request, packet);
			
			// Inform waiting threads
			synchronized(request) {
				request.notifyAll();
			}
			
			packet.finishPacket();
		}
	}
	
	private void handleResponsePacket(final Request request,
			final AbstractInPacket packet) throws IOException {
		DataInputStream in = packet.getStream();
		byte[] resp = null;
		
		if(request.getResponseType() == Packet.RESPONSE_DATA) {
			int len = in.readInt();
			resp = new byte[len];
			in.readFully(resp, 0, len);
		} else if(request.getResponseType() == Packet.RESPONSE_DISCOVERY) {
			String keyGen = in.readUTF();
			if(!keyGen.equals(this.keyGen.getName())) {
				DCSkinsLog.severe("The server uses a different key generator, "
						+ "i can't communicate with that!");
				return;
			}
			
			final byte num = in.readByte();
			for(byte i = 0; i < num; i++) {
				String compression = in.readUTF();
				compressionRegistry.addRemote(compression, in.readByte());
			}
			
			compressionUsed = compressionRegistry.getPreferencedCompression();
			if(compressionUsed == null) {
				DCSkinsLog.severe("The server can't provide any compressions"
						+ " we support. I can't connect!");
				return;
			}
			// We can send packets to the server now
			contactServer = true;
			
			// Notify waiting threads
			handshakeDone = true;
			synchronized(this) {
				notifyAll();
			}
			
			// Push out local data to the server
			for(Byte id : dataTypeRegistry.getIdentifiers()) {
				locals[id] = localDataReader.readLocal(user, id);
				if(locals[id] != null) {
					pushLocalData(id, locals[id]);
				}
			}
			
			DCSkinsLog.debug("Received DSC response!");
		}
		
		request.setResponse(resp);
	}
	
	private byte[] getLocalCacheData(final String user, final byte dataType)
			throws IOException {
		String key = keyCache.getKey(user, dataType);
		if(key == null) {
			return null;
		}
		
		byte[] data = dataCache.getData(key, dataType);
		if(data == null) {
			return null;
		}
		
		if(contactServer) {
			// Build request data structure
			Request request = new Request(user, dataType);
			// Build the request packet
			VerifyPacket packet =
				new VerifyPacket(compressionUsed, dataType, user, key);
			// Insert the request to the pending requests
			insertRequest(request);
			// Send the actual request
			packetSender.sendToServer(packet);
			
			waitForData(request);
			if(request.isResponseSet()
					&& request.getResponseType() == Packet.RESPONSE_OK) {
				// Cache data is OK
				return data;
			} else if(request.isResponseSet()
					&& request.getResponseType() == Packet.RESPONSE_DATA) {
				String newKey = keyGen.generateKey(request.getResponse());
				// Cache data is NOT OK, replace with packet data
				if(keyCache.update(user, dataType, newKey)) {
					dataCache.update(key,
							(keyCache.getKeyUseCount(key, dataType) == 0),
							newKey, dataType, request.getResponse());
				}
				return data;
			} else if(request.isResponseSet()
					&& request.getResponseType() == Packet.RESPONSE_UNKNOWN) {
				// Cache data is NOT OK, remove from cache
				keyCache.remove(user, dataType);
				dataCache.remove(key, dataType);
			}
		} else {
			return data;
		}
		return null;
	}
	
	private byte[] getServerCacheData(final String user, final byte dataType) {
		try {
			// Build request data structure
			Request request = new Request(user, dataType);
			// Build the request packet
			RequestPacket packet =
				new RequestPacket(compressionUsed, dataType, user);
			// Insert the request to the pending requests
			insertRequest(request);
			// Send the actual request
			packetSender.sendToServer(packet);
			
			waitForData(request);
			
			if(request.isResponseSet()
					&& request.getResponseType() == Packet.RESPONSE_DATA) {
				String key = keyGen.generateKey(request.getResponse());
				if(keyCache.insert(user, dataType, key)) {
					dataCache.insert(key, dataType, request.getResponse());
				}
				return request.getResponse();
			}
		} catch(IOException ignore) {
		}
		return null;
	}
	
	private void pushLocalData(final byte dataType, final byte[] data)
			throws IOException {
		DCSkinsLog.debug("Push local data type=%d", dataType);
		
		// Build the request packet
		packetSender.sendToServer(new PushPacket(compressionUsed, dataType,
				user, data));
	}
	
	private void waitForData(final Request req) {
		synchronized(req) {
			try {
				req.wait(timeout);
			} catch(InterruptedException e) {
				// This doesn't trigger as expected
				DCSkinsLog.warning("Server did not respond within " + timeout
						+ " ms");
			}
		}
	}
	
	private void insertRequest(final Request req) {
		requests.add(req);
	}
	
	private Request getRequest(final String user, final byte dataType) {
		if(requests.size() == 0) {
			return null;
		}
		Request r;
		Iterator<Request> i = requests.iterator();
		while(i.hasNext()) {
			r = i.next();
			if(r.getUser().equals(user) && r.getDataType() == dataType) {
				i.remove();
				return r;
			}
		}
		return null;
	}
}
