package com.enotes.service_impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepo;
import com.enotes.service.CategoryService;


@Service
public class categoryServiceImpl implements CategoryService{
	
	@Autowired
	private CategoryRepo categoryRepo;
	

	@Override
	public Boolean saveCategory(Category category) {
		category.setIsDeleted(false);
		Category saveCategory = categoryRepo.save(category);
		if(ObjectUtils.isEmpty(saveCategory)){
			return false;
		}
		return true;
	}

	@Override
	public List<Category> getcategory() {
		List<Category> categories = categoryRepo.findAll();
		return categories;
	}

	
}
