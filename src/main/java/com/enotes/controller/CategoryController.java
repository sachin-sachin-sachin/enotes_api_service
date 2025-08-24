package com.enotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.controllerEndpoint.CategoryEndpoint;
import com.enotes.dto.CategoryDto;
import com.enotes.dto.CategoryResponse;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.service.CategoryService;
import com.enotes.util.commonUtil;

@RestController
public class CategoryController implements CategoryEndpoint {

	
	@Autowired
	private CategoryService categoryService;

	
	@Override
	public ResponseEntity<?> saveCategory(CategoryDto categoryDto){
	
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		if(saveCategory) {
			return commonUtil.createBuildResponseMessage("Save Successfully",HttpStatus.CREATED);
		//	return new ResponseEntity<>("save Succes",HttpStatus.CREATED);
		}
		return commonUtil.createErrorResponseMessage("Not Saved",HttpStatus.INTERNAL_SERVER_ERROR);
	//	return new ResponseEntity<>("not saved",HttpStatus.INTERNAL_SERVER_ERROR) ;
	}
	
	@Override
	public ResponseEntity<?> getAllCategory(){
		List<CategoryDto> allCategory = categoryService.getcategory();
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
	//	return new ResponseEntity<>(allCategory,HttpStatus.OK);
		return  commonUtil.createBuildResponse(allCategory,HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> getActiveCategory(){
		List<CategoryResponse> allCategory = categoryService.getActiveCategory();
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
		return  commonUtil.createBuildResponse(allCategory,HttpStatus.OK);
	//	return new ResponseEntity<>(allCategory,HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> getCategoryById(Integer id) throws ResourceNotFoundException{
		
		CategoryDto categoryById = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryById)) {
			return  commonUtil.createErrorResponseMessage("Id Not Found ",HttpStatus.OK);
		//	return new ResponseEntity<>("Id Not found",HttpStatus.NOT_FOUND);
		}
		return  commonUtil.createBuildResponse(categoryById,HttpStatus.OK);
	//	return new ResponseEntity<>(categoryById,HttpStatus.OK);
	}
	
	
	@Override
	public ResponseEntity<?> deleteCategoryById(Integer id){
		
		Boolean categoryById = categoryService.deleteCategoryById(id);
	if(categoryById) {
		return  commonUtil.createBuildResponseMessage("Id Is Deleted",HttpStatus.OK);
	//	return new ResponseEntity<>("id "+id+" is deleted",HttpStatus.OK);
	}
	return  commonUtil.createErrorResponseMessage("Id Not Found ",HttpStatus.NOT_FOUND);
//	return new ResponseEntity<>("id not found",HttpStatus.NOT_FOUND);
	}
	

}
