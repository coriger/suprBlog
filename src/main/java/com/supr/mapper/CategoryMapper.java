package com.supr.mapper;

import java.util.List;

import com.supr.model.Category;


public interface CategoryMapper {

	List<Category> getCategoryList();

	Category getCategoryByAlias(String aliasName);

}
