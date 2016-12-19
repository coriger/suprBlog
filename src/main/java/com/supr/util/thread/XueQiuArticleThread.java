package com.supr.util.thread;

import com.supr.service.ArticleService;
import com.supr.util.spring.SpringContext;

/**
 * @desc	雪球问答文章自动抓取
 * @author	ljt
 * @time	2015年8月30日下午12:50:57
 */
public class XueQiuArticleThread implements Runnable{
	
	private ArticleService articleService;
	
	@Override
	public void run() {
        articleService = SpringContext.getBeanByType(ArticleService.class);
		while(true){
            try {
                Thread.sleep(24*60*60*1000);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
	}


}
