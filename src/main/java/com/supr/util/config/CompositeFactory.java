package com.supr.util.config;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;

/**
 * @desc	复合查询
 * @author	ljt
 * @time	2014-6-23 下午5:15:13
 */
public class CompositeFactory {
	
	private static CompositeConfiguration configuration;

	private CompositeFactory(){}

	public static CompositeConfiguration getInstance(){
		if(configuration == null){
			synchronized (CompositeFactory.class) {
				configuration = new CompositeConfiguration();
			}
		}
		return configuration;
	}

	public static void addConfiguration(Configuration config){
		configuration = getInstance();
		configuration.addConfiguration(config);
	}
	
	public static String getString(String key){
		return configuration.getString(key);
	}
	
}
