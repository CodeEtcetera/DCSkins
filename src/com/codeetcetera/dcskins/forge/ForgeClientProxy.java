/**
 * File: ForgeClientProxy.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

import com.google.common.base.Throwables;

import com.codeetcetera.dcskins.CommonClient;
import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.DCSkinsLog;
import com.codeetcetera.dcskins.network.ClientInPacket;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeClientProxy extends CommonClient implements IPacketHandler,
		IConnectionHandler {
	/**
	 * @throws Exception
	 */
	public ForgeClientProxy() throws Exception {
		packetSender = new ForgePacketSender();
		// Register ForgeSubscrive events
		MinecraftForge.EVENT_BUS.register(this);
		// Register for incoming packets
		NetworkRegistry.instance().registerChannel(this,
				DCSkinsCore.PACKET_CHANNEL);
		// Register for client login/logout events
		NetworkRegistry.instance().registerConnectionHandler(this);
		DCSkinsLog.debug("Registering done!");
	}
	
	/**
	 * World save handler, save the cache on world save
	 * 
	 * @param event
	 * @throws IOException
	 */
	@ForgeSubscribe
	public void onSave(final WorldEvent.Save event) throws IOException {
		DCSkinsLog.debug("World save!");
		onSave();
	}
	
	/**
	 * World unload handler, save the cache on unload
	 * 
	 * @param event
	 * @throws IOException
	 */
	@ForgeSubscribe
	public void onWorldUnload(final WorldEvent.Unload event) throws IOException {
		DCSkinsLog.debug("World unload!");
		onSave();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IPacketHandler#onPacketData(net.minecraft
	 * .network.INetworkManager,
	 * net.minecraft.network.packet.Packet250CustomPayload,
	 * cpw.mods.fml.common.network.Player)
	 */
	@Override
	public void onPacketData(final INetworkManager manager,
			final Packet250CustomPayload packet, final Player player) {
		DCSkinsLog.debug("Incoming packet!");
		try {
			onIncomingPacket(new ClientInPacket(packet.data));
		} catch(IOException e) {
			DCSkinsLog.warning("Error on incoming packet: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IConnectionHandler#playerLoggedIn(cpw.mods
	 * .fml.common.network.Player, net.minecraft.network.packet.NetHandler,
	 * net.minecraft.network.INetworkManager)
	 */
	@Override
	public void playerLoggedIn(final Player player,
			final NetHandler netHandler, final INetworkManager manager) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IConnectionHandler#connectionReceived(net
	 * .minecraft.network.NetLoginHandler,
	 * net.minecraft.network.INetworkManager)
	 */
	@Override
	public String connectionReceived(final NetLoginHandler netHandler,
			final INetworkManager manager) {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IConnectionHandler#connectionOpened(net.minecraft
	 * .network.packet.NetHandler, java.lang.String, int,
	 * net.minecraft.network.INetworkManager)
	 */
	@Override
	public void connectionOpened(final NetHandler netClientHandler,
			final String server, final int port, final INetworkManager manager) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IConnectionHandler#connectionOpened(net.minecraft
	 * .network.packet.NetHandler, net.minecraft.server.MinecraftServer,
	 * net.minecraft.network.INetworkManager)
	 */
	@Override
	public void connectionOpened(final NetHandler netClientHandler,
			final MinecraftServer server, final INetworkManager manager) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IConnectionHandler#connectionClosed(net.minecraft
	 * .network.INetworkManager)
	 */
	@Override
	public void connectionClosed(final INetworkManager manager) {
		DCSkinsLog.debug("Client logged out!");
		try {
			onLogout();
		} catch(IOException e) {
			DCSkinsLog.warning("Error on client logout: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.network.IConnectionHandler#clientLoggedIn(net.minecraft
	 * .network.packet.NetHandler, net.minecraft.network.INetworkManager,
	 * net.minecraft.network.packet.Packet1Login)
	 */
	@Override
	public void clientLoggedIn(final NetHandler clientHandler,
			final INetworkManager manager, final Packet1Login login) {
		DCSkinsLog.debug("Client logged in!");
		try {
			onLogin();
		} catch(IOException e) {
			DCSkinsLog.severe("Error on client login: %s",
					Throwables.getStackTraceAsString(e));
		}
	}
}
