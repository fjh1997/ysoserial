package org.su18.ysuserial.payloads.config;

import java.util.ArrayList;

/**
 * @author su18
 */
public class HookPointConfig {


	public static ArrayList<String> BasicServletHook = new ArrayList<String>();

	public static ArrayList<String> FilterChainHook = new ArrayList<String>();

	public static ArrayList<String> TomcatWsFilterChainHook = new ArrayList<String>();


	static {
		BasicServletHook.add("javax.servlet.http.HttpServlet");
		BasicServletHook.add("service");
		BasicServletHook.add("javax.servlet.ServletRequest,javax.servlet.ServletResponse");

		// TomEE/Tomcat org.apache.catalina.core.ApplicationFilterChain

		// InforSuite tomcat
		// AAS com.apusic.web.container.FilterChainImpl
		// BES com.bes.enterprise.webtier.core.ApplicationFilterChain
		// TongWeb com.tongweb.catalina.core.ApplicationFilterChain  com.tongweb.server.core.ApplicationFilterChain

		// GlassFish org.glassfish.grizzly.servlet.FilterChainImpl
		// Jetty org.eclipse.jetty.servlet.ServletHandler$Chain
		// Resin com.caucho.server.webapp.ContextFilterChain

		// Weblogic weblogic.servlet.internal.FilterChainImpl

		// Websphere/OpenLiberty com.ibm.ws.webcontainer.filter.WebAppFilterChain

		// JBOSS/Wildfly io.undertow.servlet.handlers.FilterHandler$FilterChainImpl or tomcat
		FilterChainHook.add("org.apache.catalina.core.ApplicationFilterChain,com.apusic.web.container.FilterChainImpl,com.bes.enterprise.webtier.core.ApplicationFilterChain,com.tongweb.catalina.core.ApplicationFilterChain,com.tongweb.server.core.ApplicationFilterChain,org.glassfish.grizzly.servlet.FilterChainImpl,org.eclipse.jetty.servlet.ServletHandler$Chain,com.caucho.server.webapp.ContextFilterChain,weblogic.servlet.internal.FilterChainImpl,com.ibm.ws.webcontainer.filter.WebAppFilterChain,io.undertow.servlet.handlers.FilterHandler$FilterChainImpl");
		FilterChainHook.add("doFilter");
		FilterChainHook.add("javax.servlet.ServletRequest,javax.servlet.ServletResponse");

		TomcatWsFilterChainHook.add("org.apache.tomcat.websocket.server.WsFilter");
		TomcatWsFilterChainHook.add("doFilter");
		TomcatWsFilterChainHook.add("javax.servlet.ServletRequest,javax.servlet.ServletResponse,javax.servlet.FilterChain");
	}

}
