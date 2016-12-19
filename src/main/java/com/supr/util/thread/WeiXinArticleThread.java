package com.supr.util.thread;

import com.supr.model.Article;
import com.supr.model.CrawlUrl;
import com.supr.model.Tag;
import com.supr.service.ArticleService;
import com.supr.util.HttpTookit;
import com.supr.util.StringUtils;
import com.supr.util.spring.SpringContext;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @desc	微信文章自动抓取
 * @author	ljt
 * @time	2015年8月30日下午12:50:57
 */
public class WeiXinArticleThread implements Runnable{
	
	private ArticleService articleService;
	
	@Override
	public void run() {
        articleService = SpringContext.getBeanByType(ArticleService.class);
		while(true){
            try {
                List<CrawlUrl> urlList = articleService.getCrawlList(0);
                if(!CollectionUtils.isEmpty(urlList)){
                    for(CrawlUrl url : urlList){
                        String result = HttpTookit.doGet(url.getUrl(),null);
                        // 删除
                        articleService.deleteCrawlById(url.getId());
                        if(StringUtils.isEmpty(result)){
                            continue;
                        }

                        Document doc = Jsoup.parse(result);
                        // 解析标题
                        String title = doc.title();
                        // 作者
                        String author = doc.getElementById("post-user").text();
                        // 解析内容
                        Element element = doc.getElementById("js_content");
                        if(null == element){
                            continue;
                        }
                        // 找到所有图片元素  依次下载并替换
                        Elements elements = element.getElementsByTag("img");
                        if(null != elements){
                            Iterator<Element> elements1 = elements.iterator();
                            while(elements1.hasNext()){
                                Element e = elements1.next();
                                String srcs = e.attr("data-src");
                                System.out.println(srcs);
                                // 下载图片
                                String rs = HttpTookit.doGetFile(srcs);
                                if(!StringUtils.isEmpty(rs)){
                                    e.attr("src","http://www.coriger.com/uploads/"+rs);
                                }
                            }
                        }

                        String content = element.html();

                        Article article = new Article();
                        article.setAuthor(author);
                        article.setTitle(title);
                        article.setContent(content);
                        article.setCategoryId(12);
                        article.setThumbUrl("201612130227435W.png");

                        if(StringUtils.isEmpty(element.text())){
                            article.setDescription(title);
                        }else if(element.text().length() > 500){
                            article.setDescription(element.text().substring(0,500));
                        }else{
                            article.setDescription(element.text());
                        }

                        List<Tag> tagList = new ArrayList<>();
                        tagList.add(new Tag(34));
                        article.setTagList(tagList);
                        // 插入db
                        articleService.addArticle(article);
                    }
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }
	}


}
