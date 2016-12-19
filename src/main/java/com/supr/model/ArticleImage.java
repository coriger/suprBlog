package com.supr.model;

/**
 * @desc	文章图片
 * @author	ljt
 * @time	2015-8-28 下午4:28:43
 */
public class ArticleImage {
	
	private Integer id;
	
	private String imageUrl;
	
	private Integer articleId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}
}
