package com.enotes.service_impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.enotes.controller.categoryController;
import com.enotes.dto.categoryDto;
import com.enotes.dto.categoryResponse;
import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepo;
import com.enotes.service.CategoryService;


@Service
public class categoryServiceImpl implements CategoryService{

	@Autowired
   private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepo categoryRepo;
	

	@Override
	public Boolean saveCategory(categoryDto categoryDto) {
		
		// old approach...
//		Category category=new Category();
//		category.setName(categoryDto.getName());
//		category.setDescription(categoryDto.getDescription());
//		category.setIsActive(categoryDto.getIsActive());
		
		
		//new approach..
		Category category = modelMapper.map(categoryDto, Category.class);
		
		
		category.setIsDeleted(false);
		category.setCreated_by(1);
		category.setCreated_on(new Date());
		Category saveCategory = categoryRepo.save(category);
		if(ObjectUtils.isEmpty(saveCategory)){
			return false;
		}
		return true;
	}

	@Override
	public List<categoryDto> getcategory() {
		List<Category> categories = categoryRepo.findByIsDeletedFalse();
		List<categoryDto> categoryDtoList = categories.stream().map(cat -> modelMapper.map(cat,categoryDto.class)).toList();
		return categoryDtoList;
	}

	@Override
	public List<categoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		List<categoryResponse> ActiveCategorylist = categories.stream().map(cat->modelMapper.map(cat,categoryResponse.class)).toList();
		return ActiveCategorylist;
	}

	@Override
	public categoryDto getCategoryById(Integer id) {
		Optional<Category> categoryById = categoryRepo.findByIdAndIsActiveTrueAndIsDeletedFalse(id);
		categoryDto categories = modelMapper.map(categoryById,categoryDto.class);
		return categories;
	}

	@Override
	public Boolean deleteCategoryById(Integer id) {
	     Optional<Category> FindByIdAndIsActiveTrue = categoryRepo.findById(id);
	   if(FindByIdAndIsActiveTrue.isPresent()) {
		   Category category=FindByIdAndIsActiveTrue.get();
		   category.setIsDeleted(true);
		   categoryRepo.save(category);
		   return true;
	   }
		return false ;
	}

	
	
}
