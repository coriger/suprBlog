package com.supr.service;

import com.supr.mapper.ArticleMapper;
import com.supr.model.Article;
import com.supr.model.CrawlUrl;
import com.supr.model.Tag;
import com.supr.model.Tip;
import com.supr.util.DateUtil;
import com.supr.util.HttpTookit;
import com.supr.util.Pager;
import com.supr.util.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(isolation=Isolation.DEFAULT,rollbackFor=Exception.class)
public class ArticleService{
	
	@Autowired
	private ArticleMapper articleMapper;
	
	public Article getArticleById(String id) {
		return articleMapper.getArticleById(id);
	}
	
	public List<Article> getArticleList(HashMap<String, Object> paramMap, Pager<Article> pager) {
		List<Article> articleList = null;
		int count = articleMapper.getArticleCount(paramMap);
		pager.setTotalCount(count);
		if(count > 0){
			paramMap.put("start", pager.getStart());
			paramMap.put("limit", pager.getLimit());
			articleList = articleMapper.getArticleList(paramMap);
		}
		return articleList;
	}
	
	public List<Article> getAllArticleList() {
		return articleMapper.getAllArticleList();
	}
	
	public int editArticle(Article article) {
		// 删除原来的文章标签关系
		articleMapper.deleteArticleTag(article);
		int count = articleMapper.editArticle(article);
		for(Tag tag : article.getTagList()){
			article.setTagId(tag.getId());
			articleMapper.addArticleTag(article);
		}
		
		// 删除原来的文章图片关系
//		articleMapper.deleteArticleImage(article);
		// 提取文章图片地址
//		List<String> imageList = getImageSrc(article.getContent());
//		if(!SuprUtil.isEmptyCollection(imageList)){
//			for(String imageUrl : imageList){
//				article.setImageUrl(imageUrl.substring(imageUrl.indexOf("uploads")+8));
//				articleMapper.addArticleImage(article);
//			}
//		}
		
		return count;
	}
	
	public int addArticle(Article article) {
		int count = articleMapper.addArticle(article);
		for (Tag tag : article.getTagList()) {
			article.setTagId(tag.getId());
			articleMapper.addArticleTag(article);
		}

		// 提取文章图片地址
//		List<String> imageList = getImageSrc(article.getContent());
//		if (!SuprUtil.isEmptyCollection(imageList)) {
//			for (String imageUrl : imageList) {
//				article.setImageUrl(imageUrl.substring(imageUrl.indexOf("uploads") + 8));
//				articleMapper.addArticleImage(article);
//			}
//		}

		return count;
	}

    public void xueqiu(){
        String result = HttpTookit.doXueqiu("https://xueqiu.com/ask/square");
        if(!StringUtils.isEmpty(result)) {
            Document doc = Jsoup.parse(result);
            // 解析内容
            Elements elements = doc.getElementsByClass("ask-item-more");
            if (null != elements) {
                Iterator<Element> detailItr = elements.iterator();
                StringBuffer sb = new StringBuffer();
                while (detailItr.hasNext()) {
                    Element e = detailItr.next();
                    String href = e.attr("href");
                    System.out.println(href);
                    result = HttpTookit.doXueqiu("https://xueqiu.com/" + href);
                    if (!StringUtils.isEmpty(result)) {
                        Document detail = Jsoup.parse(result);
                        // 问题
                        String question = detail.getElementsByClass("detail").get(0).text();
                        String rid = detail.getElementById("status-"+href.split("/")[2]).attr("data-rid");
                        String userId = detail.select(".status-retweet-user a[class='name']").get(0).attr("href").split("/")[1];
                        // 答案
                        String answer = HttpTookit.doXueqiuDetail("https://xueqiu.com/service/comment/ask?rid="+rid+"&id="+href.split("/")[2]+"&user_id="+userId);
                        // 答题人
                        Document an = Jsoup.parse(answer);
                        answer = an.getElementsByClass("detail").get(0).text();
                        // url
                        String url = "https://xueqiu.com/" + href;
                        sb.append("<font style='color:red'>问题：").append(question+"</font>").append("</br></br>")
                                .append("<p>回答：").append(answer+"</p>")
                                .append("来源:【").append(url).append("】").append("</br>")
                                .append("------------------------------------------------------------------------------------------------------------------------------------------").append("</br>");
                    }
                }
                Article article = new Article();
                article.setTitle("雪球精选问答" + DateUtil.getDay());
                article.setContent(sb.toString());
                article.setCategoryId(33);
                article.setAuthor("xueqiu");
                article.setThumbUrl("201612130227435W.png");

                if (sb.toString().length() > 500) {
                    article.setDescription(sb.toString().substring(0, 500));
                } else {
                    article.setDescription(sb.toString());
                }

                List<Tag> tagList = new ArrayList<>();
                tagList.add(new Tag(40));
                article.setTagList(tagList);
                // 插入db
                addArticle(article);
            }
        }
    }
	
	public static List<String> getImageSrc(String htmlCode) {
		List<String> imageSrcList = new ArrayList<String>();
		Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlCode);
		String quote = null;
		String src = null;
		while (m.find()) {
			quote = m.group(1);
			src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
			imageSrcList.add(src);
		}
		return imageSrcList;
	}
	
	public int deleteArticle(String id) {
//		articleMapper.deleteArticleImageById(id);
		articleMapper.deleteArticleTagById(id);
		// 删除文章
		return articleMapper.deleteArticle(id);
	}
	
	public int updateStatue(Article article) {
		return articleMapper.updateStatue(article);
	}

	public List<Article> getLastArticleList(HashMap<String, Object> paramMap) {
		List<Article> articleList = articleMapper.getLastArticleList(paramMap);
		return articleList;
	}

	public String getArticleImageUrl(String id) {
		return articleMapper.getArticleImageUrl(id);
	}

	public Article getNextArticle(HashMap<String, Object> param) {
		return articleMapper.getNextArticle(param);
	}

	public Article getBeforeArticle(HashMap<String, Object> param) {
		return articleMapper.getBeforeArticle(param);
	}

	public List<Article> getRelationArticleList(HashMap<String,Object> param) {
		return articleMapper.getRelationArticleList(param);
	}

	public void incrArticleShowCount(String id) {
		articleMapper.incrArticleShowCount(id);
	}

    public List<CrawlUrl> getCrawlList(int type) {
        return articleMapper.getCrawlList(type);
    }

    public void deleteCrawlById(Integer id) {
        articleMapper.deleteCrawlById(id);
    }

    public int addArticleTip(Tip tip) {
        return articleMapper.addArticleTip(tip);
    }

    public Tip getTip(Tip tip) {
        return articleMapper.getTip(tip);
    }

    public void addCrawlUrl(String url) {
        articleMapper.addCrawlUrl(url);
    }
}
