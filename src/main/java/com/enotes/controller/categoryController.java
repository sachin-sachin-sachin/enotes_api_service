package com.enotes.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.entity.Category;
import com.enotes.repository.CategoryRepo;
import com.enotes.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class categoryController {

    private final CategoryRepo categoryRepo;
	
	@Autowired
	private CategoryService categoryService;


    categoryController(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

	
	@PostMapping("/save_category")
	public ResponseEntity<?> saveCategory(@RequestBody Category category){
		category.setIsDeleted(false);
		category.setCreated_by(1);
		category.setCreated_on(new Date());
		Boolean saveCategory = categoryService.saveCategory(category);
		if(saveCategory) {
			return new ResponseEntity<>("save Succes",HttpStatus.CREATED);
		}
		return new ResponseEntity<>("not saved",HttpStatus.INTERNAL_SERVER_ERROR) ;
	}
	
	@GetMapping("/categories")
	public ResponseEntity<?> getCategory(){
		List<Category> allCategory = categoryService.getcategory();
		if(CollectionUtils.isEmpty(allCategory)) {
			return ResponseEntity.noContent().build();
		}
		return new ResponseEntity<>(allCategory,HttpStatus.OK);
	}
}
