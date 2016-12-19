package com.supr.util.spring;

import com.supr.util.thread.WeiXinArticleThread;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {   
	
	private static ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context=context;
        new Thread(new WeiXinArticleThread()).start();
	}
	
	public static ApplicationContext getContext() {
		return context;
	}
	
	public static <T> T getBeanByType(Class clazz) throws BeansException {
         return (T) context.getBean(clazz);
	}
	
}
