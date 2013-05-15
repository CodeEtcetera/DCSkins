/**
 * File: DCSkinsClassTransFormer.java
 * 
 */

package com.codeetcetera.dcskins.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.codeetcetera.dcskins.DCSkinsLog;

import cpw.mods.fml.relauncher.IClassTransformer;

/**
 * @author CodeEtcetera
 * 
 */
public class ClientClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(final String name, final String transformedName,
			final byte[] bytes) {
		if(transformedName.equals("net.minecraft.client.renderer"
				+ ".ThreadDownloadImageData")) {
			DCSkinsLog.debug("Transform %s-%s", name, transformedName);
			ClassReader reader = new ClassReader(bytes);
			ClassWriter writer =
				new ClassWriter(ClassWriter.COMPUTE_MAXS
						| ClassWriter.COMPUTE_FRAMES);
			ImageDataClassTransformer trans =
				new ImageDataClassTransformer(writer, transformedName.replace(
						".", "/"), name);
			reader.accept(trans, 0);
			return writer.toByteArray();
		}
		
		return bytes;
	}
}
