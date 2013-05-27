/**
 * File: MethodAdapter.java
 * 
 */
package com.codeetcetera.dcskins.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.codeetcetera.dcskins.DCSkinsImageThread;
import com.codeetcetera.dcskins.DCSkinsLog;

/**
 * @author CodeEtcetera
 * 
 */
public class ImageDataMethodAdapter extends MethodVisitor {
	private final String newClassName;
	private String oldClassName;
	
	/**
	 * @param api
	 * @param mv
	 */
	public ImageDataMethodAdapter(final MethodVisitor mv) {
		super(Opcodes.ASM4, mv);
		newClassName =
			DCSkinsImageThread.class.getCanonicalName().replace(".", "/");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.MethodVisitor#visitTypeInsn(int, java.lang.String)
	 */
	@Override
	public void visitTypeInsn(final int opcode, final String type) {
		if(opcode == Opcodes.NEW) {
			DCSkinsLog.debug("Transform new to %s\nType %s", newClassName, type);
			oldClassName = type;
			super.visitTypeInsn(opcode, newClassName);
		} else {
			super.visitTypeInsn(opcode, type);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.MethodVisitor#visitMethodInsn(int,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void visitMethodInsn(final int opcode, final String owner,
			final String name, final String desc) {
		DCSkinsLog.debug("Visit method %s.%s :%d-->%s", owner, name, opcode,
				desc);
		if(owner.equals(oldClassName)) {
			DCSkinsLog.debug("Transform %s.%s to %s\nType %s", owner, name,
					newClassName, desc);
			super.visitMethodInsn(opcode, newClassName, name, desc);
		} else {
			super.visitMethodInsn(opcode, owner, name, desc);
		}
	}
}
