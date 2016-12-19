package com.supr.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.supr.model.Category;
import com.supr.model.Friend;
import com.supr.model.Tag;
import com.supr.service.ArticleService;
import com.supr.service.CategoryService;
import com.supr.service.FriendService;
import com.supr.service.TagService;

@Controller
public class CategoryController{
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private FriendService friendService;
	
	/**
	 * 获取文章分页列表
	 */
	@RequestMapping(value = "/category/{aliasName}")
	public String loadCategoryPage(HttpSession session,ModelMap map,@PathVariable String aliasName,String articleId,String categoryId) throws UnsupportedEncodingException {
		aliasName = URLDecoder.decode(aliasName, "utf-8");
		Category category = categoryService.getCategoryByAlias(aliasName);
		
		// 栏目列表
		List<Category> categoryList = categoryService.getCategoryList();;
		
		// 标签列表
		List<Tag> tagList = tagService.getTagList();
        if(!CollectionUtils.isEmpty(tagList)){
            for(Tag tag : tagList){
                tag.setSum(tagService.getArticleCountByTag(tag.getId()));
            }
        }
		
		// 合作伙伴列表
		List<Friend> friendList = friendService.getAllFriendList();;
		
		map.put("categoryList", categoryList);
		map.put("ca", category);
		map.put("tagList", tagList);
		map.put("friendList", friendList);
		
		return "/blog/category/article_pager";
	}
	
}
