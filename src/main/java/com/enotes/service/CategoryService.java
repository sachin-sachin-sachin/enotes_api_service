package com.enotes.service;

import java.util.List;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.categoryResponse;
import com.enotes.entity.Category;
import com.enotes.exception.ResourceNotFoundException;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto);
	
	public List<CategoryDto> getcategory();

	public List<categoryResponse> getActiveCategory();

	public CategoryDto getCategoryById(Integer id) throws ResourceNotFoundException;

	public Boolean deleteCategoryById(Integer id);
}
