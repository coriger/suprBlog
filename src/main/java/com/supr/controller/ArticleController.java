package com.supr.controller;

import com.supr.model.*;
import com.supr.service.ArticleService;
import com.supr.service.CategoryService;
import com.supr.service.FriendService;
import com.supr.service.TagService;
import com.supr.util.*;
import com.supr.util.listener.SystemListener;
import com.supr.util.thread.LinkCommitThread;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

@Controller
public class ArticleController{

    @Autowired
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private FriendService friendService;
	
	/**
	 * 跳转文章列表
	 */
	@RequestMapping(value = "/admin/article/list")
	public String list(HttpSession session,ModelMap map) {
		Manager manager = (Manager)session.getAttribute(Constant.USERINFO);
		map.put("manager", manager);
		
		// 栏目列表
		List<Category> categoryList = categoryService.getCategoryList();
		// 标签列表
		List<Tag> tagList = tagService.getTagList();
				
		map.put("categoryList", categoryList);
		map.put("tagList", tagList);
		return "/admin/article/article_list";
	}
	
	/**
	 * 获取文章分页列表
	 */
	@RequestMapping(value = "/admin/article/load")
	public String loadArticlePageList(HttpSession session,ModelMap map,String param,Pager<Article> pager) throws UnsupportedEncodingException {
		Manager manager = (Manager)session.getAttribute(Constant.USERINFO);
		map.put("manager", manager);
		
		HashMap<String, Object> paramMap = new HashMap<String,Object>();
        param = "{}";
		Article article = JsonUtil.fromJson(param, Article.class);
		if(!StringUtils.isEmpty(article.getTitle())){
			article.setTitle(URLDecoder.decode(article.getTitle(), "utf-8"));
		}
		paramMap.put("article", article);
		paramMap.put("manager", manager);
		List<Article> articleList = articleService.getArticleList(paramMap, pager);
		map.put("articleList", articleList);
		map.put("pager", pager);
		return "/admin/article/article_pager_list";
	}

	/**
	 * 获取文章分页列表
	 */
	@RequestMapping(value = "/article/loadPage/{categoryId}/{articleId}")
	public String loadPage(HttpSession session,ModelMap map,@PathVariable String articleId,@PathVariable String categoryId) throws UnsupportedEncodingException {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("categoryId", categoryId);
		paramMap.put("articleId", articleId);
		// 最新的文章列表
		List<Article> articleList = articleService.getLastArticleList(paramMap);
		if(!SuprUtil.isEmptyCollection(articleList)){
			int i = 1;
			for(Article article : articleList){
				// 获取标签
				List<Tag> tList = tagService.getArticleTagList(String.valueOf(article.getId()));
				article.setTagList(tList);
				if(i == articleList.size()){
					map.put("articleId", article.getId());
				}
				i++;
			}
		}
		map.put("articleList", articleList);
		return "/blog/article/article_pager";
	}
	
	/**
	 * 跳转文章新增页面
	 */
	@RequestMapping(value = "/admin/article/addJump")
	public String addJump(HttpSession session,ModelMap map){
		Manager manager = (Manager)session.getAttribute(Constant.USERINFO);
		map.put("manager", manager);
		
		// 栏目列表  链接栏目
		List<Category> categoryList = categoryService.getCategoryList();
		// 标签列表
		List<Tag> tagList = tagService.getTagList();
				
		map.put("categoryList", categoryList);
		map.put("tagList", tagList);
		return "/admin/article/add_article";
	}
	
	/**
	 * 跳转文章编辑页面
	 */
	@RequestMapping(value = "/article/html/{id}")
	public String articleDetail(HttpSession session, ModelMap map,@PathVariable String id) {
		// 更新文章的浏览数
		articleService.incrArticleShowCount(id);
		// 栏目列表
		List<Category> categoryList = categoryService.getCategoryList();
		// 标签列表
		List<Tag> tagList = tagService.getTagList();
        if(!CollectionUtils.isEmpty(tagList)){
            for(Tag tag : tagList){
                tag.setSum(tagService.getArticleCountByTag(tag.getId()));
            }
        }
		// 合作伙伴列表
		List<Friend> friendList = friendService.getAllFriendList();
		// 文章详情
		Article article = articleService.getArticleById(id);
		// 获取标签
		List<Tag> tList = tagService.getArticleTagList(id);
		article.setTagList(tList);
		
		HashMap<String,Object> param = new HashMap<String,Object>();
		param.put("articleId", id);
		param.put("categoryId", article.getCategoryId());
		// 获取上一个文章 下一个文章id
		Article beforeArticle = articleService.getBeforeArticle(param);
		Article nextArticle = articleService.getNextArticle(param);
		
		// 获取相关文章  4个  同类型的随机四个
		List<Article> relationList = articleService.getRelationArticleList(param);
		map.put("beforeArticle", beforeArticle);
		map.put("nextArticle", nextArticle);
		map.put("article", article);
		map.put("relationList", relationList);
		map.put("categoryList", categoryList);
		map.put("tagList", tagList);
		map.put("friendList", friendList);
		
		return "/blog/article/article_detail";
	}

    @RequestMapping(value = "/article/tip")
    public @ResponseBody Result tip(Tip tip,HttpSession session) throws UnsupportedEncodingException {
        tip.setContent(URLDecoder.decode(tip.getContent(), "utf-8"));
        String content = tip.getContent();
        if(!content.endsWith("lujiangtao123456")){
            return new Result("auth", Constant.AUTH_FAIL);
        }else{
            content = content.replaceAll("lujiangtao123456","");
            tip.setContent(content);
        }
        if(articleService.addArticleTip(tip) > 0){
            return new Result("success", Constant.DEAL_SUCCESS);
        }else{
            return new Result("fail", Constant.DEAL_FAIL);
        }
    }

    /**
     * 添加文章
     */
    @RequestMapping(value = "/article/gettip")
    public void tip(Tip tip, HttpServletResponse response) throws Exception {
        Tip tp = articleService.getTip(tip);
        response.setCharacterEncoding("utf-8");
        if(null != tp){
              response.getWriter().write(tp.getContent());
        }
    }

    @RequestMapping(value = "/article/wx")
    public void tip(String url,String timestamp,String ver,String signature, HttpServletResponse response) throws Exception {
        url = url + "&timestamp=" + timestamp + "&ver=" + ver + "&signature=" + signature;
        if(!url.contains("mp.weixin.qq.com")){
            return;
        }
        articleService.addCrawlUrl(url);
    }

    @RequestMapping(value = "/xueqiu")
    public void tip(HttpServletResponse response) throws Exception {
        articleService.xueqiu();
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("success");
    }
	
	/**
	 * 添加文章
	 */
	@RequestMapping(value = "/admin/article/add")
	public @ResponseBody Result addArticle(String description,String content,String param,HttpSession session) throws UnsupportedEncodingException {
		param = URLDecoder.decode(param,"utf-8");
		content = URLDecoder.decode(content,"utf-8");
		description = URLDecoder.decode(description,"utf-8");
		Article article = JsonUtil.fromJson(param, Article.class);
		article.setContent(content);
		article.setDescription(description);
		if(articleService.addArticle(article) > 0){
			// 提交百度链接收录
			LinkCommitThread.addLink(String.valueOf(article.getId().intValue()));
			return new Result("success", Constant.DEAL_SUCCESS);
		}else{
			return new Result("fail", Constant.DEAL_FAIL);
		}
	}
	
	/**
	 * 删除文章
	 */
	@RequestMapping(value = "/admin/article/delete")
	public @ResponseBody Result deleteArticle(String id) {
		if(articleService.deleteArticle(id) > 0){
			return new Result("success", Constant.DEAL_SUCCESS);
		}else{
			return new Result("fail", Constant.DEAL_FAIL);
		}
	}
	
	/**
	 * 跳转文章编辑页面
	 */
	@RequestMapping(value = "/admin/article/editJump/{id}")
	public String editJump(HttpSession session,ModelMap map, @PathVariable String id) {
		Manager manager = (Manager)session.getAttribute(Constant.USERINFO);
		map.put("manager", manager);
		// 栏目列表 链接栏目
		List<Category> categoryList = categoryService.getCategoryList();
		// 标签列表
		List<Tag> allTagList = tagService.getTagList();

		Article article = articleService.getArticleById(id);
		List<Tag> tagList = tagService.getArticleTagList(id);
		
		map.put("article", article);
		map.put("categoryList", categoryList);
		map.put("tagList", tagList);
		map.put("allTagList", allTagList);
		map.put("articleId", id);
		return "/admin/article/edit_article";
	}
	
	/**
	 * 变更状态
	 */
	@RequestMapping(value = "/admin/article/updateStatue")
	public @ResponseBody Result updateStatue(Article article) {
		if(articleService.updateStatue(article) > 0){
			return new Result("success", Constant.DEAL_SUCCESS);
		}else{
			return new Result("fail", Constant.DEAL_FAIL);
		}
	}
	
	/**
	 * 编辑文章
	 */
	@RequestMapping(value = "/admin/article/edit")
	public @ResponseBody Result editArticle(String description,String content,String param,HttpSession session) throws UnsupportedEncodingException {
		content = URLDecoder.decode(content,"utf-8");
		description = URLDecoder.decode(description,"utf-8");
		param = URLDecoder.decode(param,"utf-8");
		Article article = JsonUtil.fromJson(param, Article.class);
		article.setContent(content);
		article.setDescription(description);
		if(articleService.editArticle(article) > 0){
			// 删除文章
			File file = new File(SystemListener.getContext().getRealPath("/")+"/html/"+article.getId().intValue());
			if(file.exists()){
				file.delete();
			}
			return new Result("success", Constant.DEAL_SUCCESS);
		}else{
			return new Result("fail", Constant.DEAL_FAIL);
		}
	}
	
	/**
	 * 重新静态化文章
	 */
	@RequestMapping(value = "/admin/article/staticAll")
	public @ResponseBody Result staticAllArticle(HttpSession session){
		List<Article> articleList = articleService.getAllArticleList();
		if(!SuprUtil.isEmptyCollection(articleList)){
			for(Article article : articleList){
				// 删除文章
				File file = new File(SystemListener.getContext().getRealPath("/")+"/html/"+article.getId().intValue());
				if(file.exists()){
					file.delete();
				}
				
				// 模拟请求 静态化
				String articleUrl = Constant.HOST_URL + "/article/html/"+article.getId().intValue();
				HttpTookit.doGet(articleUrl, null);
			}
		}
		
		return new Result("success", Constant.DEAL_SUCCESS);
	}
	
	/**
	 * 重新静态化文章
	 */
	@RequestMapping(value = "/admin/article/static/{id}")
	public @ResponseBody Result staticArticle(@PathVariable String id,HttpSession session){
		Article article = articleService.getArticleById(id);
		// 删除文章
		File file = new File(SystemListener.getContext().getRealPath("/")+"/html/"+article.getId().intValue());
		if(file.exists()){
			file.delete();
		}
		
		// 模拟请求 静态化
		String articleUrl = Constant.HOST_URL + "/article/html/"+id;
		HttpTookit.doGet(articleUrl, null);
		return new Result("success", Constant.DEAL_SUCCESS);
	}
}
