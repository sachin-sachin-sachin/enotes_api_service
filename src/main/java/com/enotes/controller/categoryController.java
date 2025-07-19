package com.enotes.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.entity.Category;
import com.enotes.dto.categoryDto;
import com.enotes.dto.categoryResponse;
import com.enotes.repository.CategoryRepo;
import com.enotes.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class categoryController {

	
	@Autowired
	private CategoryService categoryService;


	
	@PostMapping("/save_category")
	public ResponseEntity<?> saveCategory(@RequestBody categoryDto categoryDto){
	
		Boolean saveCategory = categoryService.saveCategory(categoryDto);
		if(saveCategory) {
			return new ResponseEntity<>("save Succes",HttpStatus.CREATED);
		}
		return new ResponseEntity<>("not saved",HttpStatus.INTERNAL_SERVER_ERROR) ;
	}
	
	@GetMapping("/categories")
	public ResponseEntity<?> getCategory(){
		List<categoryDto> allCategory = categoryService.getcategory();
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<>(allCategory,HttpStatus.OK);
	}
	
	
	@GetMapping("/getIsActiveCategories")
	public ResponseEntity<?> getCategoryResponseIsActive(){
		List<categoryResponse> allCategory = categoryService.getActiveCategory();
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<>(allCategory,HttpStatus.OK);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getCategoryById(@PathVariable Integer id){
		
		categoryDto categoryById = categoryService.getCategoryById(id);
		if(ObjectUtils.isEmpty(categoryById)) {
			return new ResponseEntity<>("Id Not found",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(categoryById,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCategoryById(@PathVariable Integer id){
		
		Boolean categoryById = categoryService.deleteCategoryById(id);
	if(categoryById) {
		return new ResponseEntity<>("id "+id+" is deleted",HttpStatus.OK);
	}
	return new ResponseEntity<>("id not found",HttpStatus.NOT_FOUND);
	}
	

}
