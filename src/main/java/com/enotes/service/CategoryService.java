package com.enotes.service;

import java.util.List;

import com.enotes.dto.categoryDto;
import com.enotes.dto.categoryResponse;
import com.enotes.entity.Category;

public interface CategoryService {

	public Boolean saveCategory(categoryDto categoryDto);
	
	public List<categoryDto> getcategory();

	public List<categoryResponse> getActiveCategory();
}
