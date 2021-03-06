/**
 * File: ForgeModContainer.java
 * 
 */
package com.codeetcetera.dcskins.forge;

import java.io.File;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;

import com.codeetcetera.dcskins.DCSkinsCore;
import com.codeetcetera.dcskins.DCSkinsLog;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;

/**
 * @author CodeEtcetera
 * 
 */
public class ForgeModContainer implements ModContainer {
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getModId()
	 */
	@Override
	public String getModId() {
		return "DCSkins";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getName()
	 */
	@Override
	public String getName() {
		return "Decentralized skins";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getVersion()
	 */
	@Override
	public String getVersion() {
		return DCSkinsCore.MOD_VERSION;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getSource()
	 */
	@Override
	public File getSource() {
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getMetadata()
	 */
	@Override
	public ModMetadata getMetadata() {
		ModMetadata meta = new ModMetadata();
		meta.modId = getModId();
		meta.authorList = Arrays.asList(new String[] {"CodeEtcetera"});
		meta.name = "Decentralized skins";
		meta.description = "";
		meta.autogenerated = false;
		
		return meta;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#bindMetadata(cpw.mods.fml.common.
	 * MetadataCollection)
	 */
	@Override
	public void bindMetadata(final MetadataCollection mc) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#setEnabledState(boolean)
	 */
	@Override
	public void setEnabledState(final boolean enabled) {
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getRequirements()
	 */
	@Override
	public Set<ArtifactVersion> getRequirements() {
		return Collections.emptySet();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getDependencies()
	 */
	@Override
	public List<ArtifactVersion> getDependencies() {
		return Collections.emptyList();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getDependants()
	 */
	@Override
	public List<ArtifactVersion> getDependants() {
		return Collections.emptyList();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getSortingRules()
	 */
	@Override
	public String getSortingRules() {
		return "";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.common.ModContainer#registerBus(com.google.common.eventbus
	 * .EventBus, cpw.mods.fml.common.LoadController)
	 */
	@Override
	public boolean registerBus(final EventBus bus,
			final LoadController controller) {
		bus.register(ForgeDCSkins.getInstance());
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#matches(java.lang.Object)
	 */
	@Override
	public boolean matches(final Object mod) {
		DCSkinsLog.info("Matches called: %s/%s", mod,
				ForgeDCSkins.getInstance());
		return mod != null && mod.equals(ForgeDCSkins.getInstance());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getMod()
	 */
	@Override
	public Object getMod() {
		DCSkinsLog.info("Request for mod instance!");
		return ForgeDCSkins.getInstance();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getProcessedVersion()
	 */
	@Override
	public ArtifactVersion getProcessedVersion() {
		return new DefaultArtifactVersion(DCSkinsCore.MOD_VERSION);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#isImmutable()
	 */
	@Override
	public boolean isImmutable() {
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#isNetworkMod()
	 */
	@Override
	public boolean isNetworkMod() {
		/*
		 * Setting this to true crashes on login with a NPE at the check that
		 * the server has the mod installed.
		 * Since we don't require the server to have the mod installed we can
		 * set this to false.
		 */
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getDisplayVersion()
	 */
	@Override
	public String getDisplayVersion() {
		return DCSkinsCore.MOD_VERSION;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#acceptableMinecraftVersionRange()
	 */
	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		try {
			return VersionRange.createFromVersionSpec(DCSkinsCore.MC_VERSION);
		} catch(InvalidVersionSpecificationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.common.ModContainer#getSigningCertificate()
	 */
	@Override
	public Certificate getSigningCertificate() {
		return null;
	}
}
