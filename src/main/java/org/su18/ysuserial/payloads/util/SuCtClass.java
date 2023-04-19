package org.su18.ysuserial.payloads.util;

import javassist.CannotCompileException;
import javassist.CtClass;

import java.io.IOException;

/**
 * @author su18
 */
public class SuCtClass {

	public static CtClass CT_CLASS;

	public SuCtClass(CtClass ctClass) {
		CT_CLASS = ctClass;
	}


	public byte[] toBytecode() throws IOException, CannotCompileException {
		byte[] bytes = CT_CLASS.toBytecode();

		// 写入前将 classBytes 中的类标识设为 JDK 1.6 的版本号
		bytes[7] = 49;
		return bytes;
	}

	public String getName() {
		return CT_CLASS.getName();
	}

	public CtClass getCtClass() {
		return CT_CLASS;
	}
}
