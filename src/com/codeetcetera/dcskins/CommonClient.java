/**
 * File: CommonClient.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.codeetcetera.dcskins.network.DiscoveryPacket;
import com.codeetcetera.dcskins.network.InPacket;
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
	protected String user;
	protected byte[][] locals;
	protected LinkedList<Request> requests;
	protected boolean contactServer;
	protected byte compressionId;
	
	private final int timeout;
	
	/**
	 * @throws Exception
	 */
	public CommonClient() throws Exception {
		user = null;
		locals = new byte[16][];
		requests = new LinkedList<Request>();
		contactServer = false;
		
		compressionId = 0;
		
		timeout =
			DCSkinsConfig.getInstance().getIntProp("client.waitforservertime");
	}
	
	/**
	 * @param user
	 * @throws IOException
	 */
	public void onLogin(final String user) throws IOException {
		this.user = null;
		
		// Send a DSC to the server
		Request req = new Request("", (byte) 0);
		insertRequest(req);
		packetSender.sendToServer(new DiscoveryPacket());
		
		// Wait for the DSC to return
		waitForData(req);
		
		// Load the local data
		for(Byte id : dataTypeRegistry.getIdentifiers()) {
			locals[id] = localDataReader.readLocal(user, id);
			if(locals[id] != null) {
				updateCache(user, id, locals[id]);
				// Update local cache & server cache
				if(contactServer) {
					pushLocalData(id, locals[id]);
				}
			}
		}
		
		// Save local cache
		onSave();
		
		this.user = user;
		synchronized(this) {
			notifyAll();
		}
	}
	
	/**
	 * @throws IOException
	 */
	public void onLogout() throws IOException {
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
		// Client logged in event not triggered yet, wait for it to finish
		if(this.user == null) {
			synchronized(this) {
				try {
					// Double check
					if(this.user == null) {
						wait();
					}
				} catch(InterruptedException ignored) {
				}
			}
		}
		
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
	}
	
	@Override
	protected void handlePacket(final byte dataType, final String user,
			final InPacket packet) throws IOException {
		if(packet.getPacketType() != Packet.PACKETTYPE_RSP) {
			super.handlePacket(dataType, user, packet);
		} else {
			// Get intial request
			Request request = getRequest(user, dataType);
			if(request == null) {
				return;
			}
			
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
			final InPacket packet) throws IOException {
		DataInputStream in = packet.getStream();
		byte[] resp = null;
		
		if(request.getResponseType() == Packet.RESPONSE_DATA) {
			int len = in.readInt();
			resp = new byte[len];
			in.readFully(resp, 0, len);
		} else if(request.getResponseType() == Packet.RESPONSE_DISCOVERY) {
			String keyGen = in.readUTF();
			if(!keyGen.equals(this.keyGen.getName())) {
				DCSkinsLog.warning("The server uses a different key generator, "
						+ "i can't communicate with that!");
				return;
			}
			
			// TODO: Load priority from config & apply priority
			final byte num = in.readByte();
			String[] compressions = new String[num];
			byte[] serverIndices = new byte[num];
			String compressionUsed = null;
			byte serverIdx = -1;
			for(byte i = 0; i < num; i++) {
				compressions[i] = in.readUTF();
				serverIndices[i] = in.readByte();
				if(serverIndices[i] > serverIdx) {
					serverIdx = serverIndices[i];
					compressionUsed = compressions[i];
				}
			}
			
			byte clientIdx =
				compressionRegistry.getCompressionIdx(compressionUsed);
			compressionRegistry.swap(clientIdx, serverIdx);
			compressionId = serverIdx;
			// We can send packets to the server now
			contactServer = true;
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
				new VerifyPacket(compressionId, dataType, user, key);
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
				new RequestPacket(compressionId, dataType, user);
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
		packetSender.sendToServer(new PushPacket(compressionId, dataType, user,
				data));
	}
	
	private void waitForData(final Request req) {
		synchronized(req) {
			try {
				req.wait(timeout);
			} catch(InterruptedException e) {
				DCSkinsLog.warning("Server did not respond within " + timeout
						+ " ms");
			}
		}
	}
	
	private void insertRequest(final Request req) {
		synchronized(requests) {
			requests.add(req);
		}
	}
	
	private Request getRequest(final String user, final byte dataType) {
		if(requests.size() == 0) {
			return null;
		}
		synchronized(requests) {
			Request r;
			Iterator<Request> i = requests.iterator();
			while(i.hasNext()) {
				r = i.next();
				if(r.getUser().equals(user) && r.getDataType() == dataType) {
					i.remove();
					return r;
				}
			}
		}
		return null;
	}
}
