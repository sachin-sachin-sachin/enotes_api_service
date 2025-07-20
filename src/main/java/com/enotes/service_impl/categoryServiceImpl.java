package com.enotes.service_impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.categoryDto;
import com.enotes.dto.categoryResponse;
import com.enotes.entity.Category;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.repository.CategoryRepo;
import com.enotes.service.CategoryService;

@Service
public class categoryServiceImpl implements CategoryService {

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

		// new approach..
		Category category = modelMapper.map(categoryDto, Category.class);
		if (ObjectUtils.isEmpty(category.getId())) {
			category.setIsDeleted(false);
			category.setCreated_by(1);
			category.setCreated_on(new Date());
		} else {
			updateCategory(category);
		}

		Category saveCategory = categoryRepo.save(category);
		if (ObjectUtils.isEmpty(saveCategory)) {
			return false;
		}
		return true;
	}

	private void updateCategory(Category category) {
		Optional<Category> findById = categoryRepo.findById(category.getId());
		if (findById.isPresent()) {
			Category category2 = findById.get();
			category.setCreated_by(category2.getCreated_by());
			category.setIsDeleted(category2.getIsDeleted());
			category.setCreated_on(category2.getCreated_on());

			category.setUpdated_by(1);
			category.setUpdated_on(new Date());
		}
	}

	@Override
	public List<categoryDto> getcategory() {
		List<Category> categories = categoryRepo.findByIsDeletedFalse();
		List<categoryDto> categoryDtoList = categories.stream().map(cat -> modelMapper.map(cat, categoryDto.class))
				.toList();
		return categoryDtoList;
	}

	@Override
	public List<categoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepo.findByIsActiveTrueAndIsDeletedFalse();
		List<categoryResponse> ActiveCategorylist = categories.stream()
				.map(cat -> modelMapper.map(cat, categoryResponse.class)).toList();
		return ActiveCategorylist;
	}

	@Override
	public categoryDto getCategoryById(Integer id) throws ResourceNotFoundException {
		Category categoryById = categoryRepo.findByIdAndIsDeletedFalse(id).orElseThrow(()->new ResourceNotFoundException("Id Not Found With "+id));
		if (!ObjectUtils.isEmpty(categoryById)) {
			categoryDto categories = modelMapper.map(categoryById, categoryDto.class);	
			return categories;	
		}
	return null;
	}

	@Override
	public Boolean deleteCategoryById(Integer id) {
		Optional<Category> FindByIdAndIsActiveTrue = categoryRepo.findById(id);
		if (FindByIdAndIsActiveTrue.isPresent()) {
			Category category = FindByIdAndIsActiveTrue.get();
			category.setIsDeleted(true);
			categoryRepo.save(category);
			return true;
		}
		return false;
	}

}
