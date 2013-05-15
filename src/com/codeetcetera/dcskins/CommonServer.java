/**
 * File: CommonServer.java
 * 
 */
package com.codeetcetera.dcskins;

import java.io.DataInputStream;
import java.io.IOException;

import com.codeetcetera.dcskins.cache.IDataCache;
import com.codeetcetera.dcskins.cache.IKeyCache;
import com.codeetcetera.dcskins.cache.IKeyGenerator;
import com.codeetcetera.dcskins.compression.CompressionRegistry;
import com.codeetcetera.dcskins.compression.GzipCompressionStream;
import com.codeetcetera.dcskins.compression.ICompressionStreamProvider;
import com.codeetcetera.dcskins.compression.NoCompressionStream;
import com.codeetcetera.dcskins.datatypes.CapeDataType;
import com.codeetcetera.dcskins.datatypes.DataTypeRegistry;
import com.codeetcetera.dcskins.datatypes.SkinDataType;
import com.codeetcetera.dcskins.network.InPacket;
import com.codeetcetera.dcskins.network.Packet;
import com.codeetcetera.dcskins.network.ResponseDataPacket;
import com.codeetcetera.dcskins.network.ResponseDiscoveryPacket;
import com.codeetcetera.dcskins.network.ResponseOKPacket;
import com.codeetcetera.dcskins.network.ResponseUnknownPacket;

/**
 * @author CodeEtcetera
 * 
 */
public abstract class CommonServer {
	protected DataTypeRegistry dataTypeRegistry;
	protected CompressionRegistry compressionRegistry;
	
	protected IPacketSender packetSender;
	protected IKeyGenerator keyGen;
	protected IKeyCache keyCache;
	protected IDataCache dataCache;
	protected LocalDataReader localDataReader;
	
	protected byte[][] defaults;
	
	private long lastKeyCacheUpdate;
	private long lastKeyCacheChange;
	private long lastDataCacheUpdate;
	private long lastDataCacheChange;
	
	/**
	 * @throws Exception
	 */
	public CommonServer() throws Exception {
		compressionRegistry = CompressionRegistry.getInstance();
		dataTypeRegistry = DataTypeRegistry.getInstance();
		
		// Register default compression types
		compressionRegistry.register(new NoCompressionStream());
		compressionRegistry.register(new GzipCompressionStream());
		
		// Register default data types
		dataTypeRegistry.register(new SkinDataType());
		dataTypeRegistry.register(new CapeDataType());
	}
	
	public void init() throws Exception {
		// Verify files & directory's
		StartupVerifier verifier = new StartupVerifier();
		verifier.verifyGlobal();
		verifier.verifyCache();
		
		DCSkinsConfig config = DCSkinsConfig.getInstance();
		GzipCompressionStream gzip = new GzipCompressionStream();
		keyGen =
			(IKeyGenerator) Class.forName(
					config.getStringProp("cache.keygen.class"))
									.getConstructor().newInstance();
		keyCache =
			(IKeyCache) Class.forName(
					config.getStringProp("cache.keycache.class"))
								.getConstructor(
										ICompressionStreamProvider.class,
										IKeyGenerator.class)
								.newInstance(gzip, keyGen);
		dataCache =
			(IDataCache) Class.forName(
					config.getStringProp("cache.datacache.class"))
								.getConstructor(
										ICompressionStreamProvider.class)
								.newInstance(gzip);
		
		// Load local defaults
		localDataReader = new LocalDataReader();
		
		defaults = new byte[16][];
		for(Byte id : dataTypeRegistry.getIdentifiers()) {
			defaults[id] = localDataReader.readLocal("char", id);
			if(defaults[id] != null) {
				DCSkinsLog.debug("Load default data for type " + id);
			}
		}
		
		// Init the last updated time
		updateLastKeyCacheChange();
		updateLastDataCacheChange();
		lastKeyCacheUpdate = System.currentTimeMillis();
		lastDataCacheUpdate = lastKeyCacheUpdate;
	}
	
	public void onIncomingPacket(final InPacket packet) throws IOException {
		DataInputStream in = packet.getStream();
		
		byte first = in.readByte();
		// Only handle the request if the protocol version matches
		if(packet.usesCorrectProtocol()) {
			if(packet.getPacketType() != Packet.PACKETTYPE_DSC) {
				byte dataType = in.readByte();
				String user = in.readUTF();
				handlePacket(dataType, user, packet);
			} else {
				handleDiscovery(packet);
			}
		}
		// Close the stream
		packet.finishPacket();
	}
	
	protected void handlePacket(final byte dataType, final String user,
			final InPacket packet) throws IOException {
		switch(packet.getPacketType()) {
			case Packet.PACKETTYPE_REQ:
				handleRequest(dataType, user, packet);
				break;
			case Packet.PACKETTYPE_PSH:
				handlePush(dataType, user, packet);
				break;
			case Packet.PACKETTYPE_VFY:
				handleVerify(dataType, user, packet);
				break;
			default:
				// Server ignores requests with unknown types
				return;
		}
	}
	
	public void onSave() throws IOException {
		if(lastKeyCacheChange > lastKeyCacheUpdate) {
			keyCache.onSave();
			lastKeyCacheUpdate = System.currentTimeMillis();
		}
		
		if(lastDataCacheChange > lastDataCacheUpdate) {
			dataCache.onSave();
			lastDataCacheUpdate = System.currentTimeMillis();
		}
	}
	
	// Handle a request for data
	private void handleRequest(final byte dataType, final String user,
			final InPacket packet) throws IOException {
		DCSkinsLog.debug("S REQ: " + user + "~" + dataType);
		byte[] data = null;
		if(keyCache.inCache(user, dataType)) {
			String key = keyCache.getKey(user, dataType);
			if(dataCache.inCache(key, dataType)) {
				data = dataCache.getData(key, dataType);
			}
		}
		
		if(data == null && defaults[dataType] != null) {
			data = defaults[dataType];
		}
		
		if(data == null) {
			packetSender.sendPlayerResponse(
					new ResponseUnknownPacket(packet.getCompression(),
							dataType, user), packet);
		} else {
			packetSender.sendPlayerResponse(
					new ResponseDataPacket(packet.getCompression(), dataType,
							user, data), packet);
		}
	}
	
	// Handle data being pushed to the server
	private void handlePush(final byte dataType, final String user,
			final InPacket packet) throws IOException {
		DataInputStream in = packet.getStream();
		DCSkinsLog.debug("S PSH: " + user + "~" + dataType);
		// Length of the data
		int len = in.readInt();
		
		if(len > 0) {
			// New data request
			byte[] data = new byte[len];
			in.readFully(data, 0, len);
			
			updateCache(user, dataType, data);
		} else {
			// Remove request
			if(keyCache.inCache(user, dataType)) {
				String key = keyCache.getKey(user, dataType);
				dataCache.remove(key, dataType);
				keyCache.remove(user, dataType);
				updateLastKeyCacheChange();
				updateLastDataCacheChange();
			}
		}
		
		// Do not respond with an OK, the client has no use for a response here
	}
	
	protected void updateCache(final String user, final byte dataType,
			final byte[] data) throws IllegalArgumentException, IOException {
		if(keyCache.inCache(user, dataType)) {
			// Update data
			String oldKey = keyCache.getKey(user, dataType);
			String newKey = keyGen.generateKey(data);
			if(keyCache.update(user, dataType, newKey)) {
				dataCache.update(oldKey,
						(keyCache.getKeyUseCount(oldKey, dataType) == 0),
						newKey, dataType, data);
			}
		} else {
			// Insert new data
			String key = keyGen.generateKey(data);
			keyCache.insert(user, dataType, key);
			dataCache.insert(key, dataType, data);
		}
		updateLastKeyCacheChange();
		updateLastDataCacheChange();
	}
	
	// Handle a verification of the client cache request
	private void handleVerify(final byte dataType, final String user,
			final InPacket packet) throws IOException {
		DCSkinsLog.debug("S VFY: " + user + "~" + dataType);
		// Read the key as UTF-8
		String key = packet.getStream().readUTF();
		
		if(key.equals(keyCache.getKey(user, dataType))) {
			packetSender.sendPlayerResponse(
					new ResponseOKPacket(packet.getCompression(), dataType,
							user), packet);
		} else {
			// Verify failed, send the correct data if known
			handleRequest(dataType, user, packet);
		}
	}
	
	// Handle a discovery request
	private void handleDiscovery(final InPacket packet) throws IOException {
		DataInputStream in = packet.getStream();
		DCSkinsLog.debug("S DSC");
		
		if(this instanceof CommonClient) {
			/*
			 * Server is also the client itself (LAN server) send unknown back
			 * to satisfy the request.
			 */
			packetSender.sendPlayerResponse(new ResponseUnknownPacket((byte) 0,
					(byte) 0, ""), packet);
			return;
		}
		
		final byte num = in.readByte();
		
		byte[] indices = new byte[num];
		byte writePtr = 0;
		CompressionRegistry reg = CompressionRegistry.getInstance();
		for(byte i = 0; i < num; i++) {
			byte idx = reg.getCompressionIdx(in.readUTF());
			
			// Does the server know this compression
			if(idx != -1) {
				indices[writePtr++] = idx;
			}
		}
		
		packetSender.sendPlayerResponse(
				new ResponseDiscoveryPacket(keyGen.getName(), indices, writePtr),
				packet);
	}
	
	protected void updateLastKeyCacheChange() {
		lastKeyCacheChange = System.currentTimeMillis();
	}
	
	protected void updateLastDataCacheChange() {
		lastDataCacheChange = System.currentTimeMillis();
	}
}
