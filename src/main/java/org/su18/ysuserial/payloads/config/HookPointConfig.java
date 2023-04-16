package org.su18.ysuserial.payloads.config;

import java.util.ArrayList;

/**
 * @author su18
 */
public class HookPointConfig {


	public static ArrayList<String> BasicServletHook = new ArrayList<String>();

	public static ArrayList<String> TomcatFilterChainHook = new ArrayList<String>();

	public static ArrayList<String> TomcatWsFilterChainHook = new ArrayList<String>();


	static {
		BasicServletHook.add("javax.servlet.http.HttpServlet");
		BasicServletHook.add("service");
		BasicServletHook.add("javax.servlet.ServletRequest,javax.servlet.ServletResponse");

		TomcatFilterChainHook.add("org.apache.catalina.core.ApplicationFilterChain");
		TomcatFilterChainHook.add("doFilter");
		TomcatFilterChainHook.add("javax.servlet.ServletRequest,javax.servlet.ServletResponse");

		TomcatWsFilterChainHook.add("org.apache.tomcat.websocket.server.WsFilter");
		TomcatWsFilterChainHook.add("doFilter");
		TomcatWsFilterChainHook.add("javax.servlet.ServletRequest,javax.servlet.ServletResponse,javax.servlet.FilterChain");
	}

}
