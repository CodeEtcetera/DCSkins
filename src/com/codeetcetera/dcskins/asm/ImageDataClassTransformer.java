/**
 * File: ClassTransformer.java
 * 
 */
package com.codeetcetera.dcskins.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author CodeEtcetera
 * 
 */
public class ImageDataClassTransformer extends ClassVisitor {
	
	/**
	 * @param api
	 * @param cv
	 */
	public ImageDataClassTransformer(final ClassVisitor cv) {
		super(Opcodes.ASM4, cv);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.asm.ClassVisitor#visitMethod(int, java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {
		MethodVisitor mv =
			super.visitMethod(access, name, desc, signature, exceptions);
		if(name.equals("<init>")) {
			return new ImageDataMethodAdapter(mv);
		}
		
		return mv;
	}
}
