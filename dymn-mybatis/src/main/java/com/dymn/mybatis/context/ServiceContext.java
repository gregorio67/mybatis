package com.dymn.mybatis.context;

public class ServiceContext {

	private static ThreadLocal<ServiceInfos> context = new ThreadLocal<>();
	
	/**
	 * 
	 * @return
	 */
	public static ServiceInfos getContext() {
		return context.get() != null ? context.get() : new ServiceInfos();
	}
	
	/**
	 * Set context
	 * @param commonInfo
	 */
	public static void setContext(ServiceInfos serviceInfos) {
		context.set(serviceInfos);
	}
	
	/**
	 * Remove context
	 */
	public static void removeContext() {
		context.remove();
	}
		
}
