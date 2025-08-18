package com.enotes.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.enums.TodoStatus;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.exception.validationException;

@Component
public class Validation {
	
	
	

	public void categoryValidation(CategoryDto categoryDto) {
		
		Map<String,Object>errors=new LinkedHashMap<>();
		
		if(ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("category object/json cannot be empty or null");
		}else {  
			
		// Name Validation
			if(ObjectUtils.isEmpty(categoryDto.getName())) {
			errors.put("name", "name Field cannot be empty or null");	
			}else {
				if(categoryDto.getName().length()<3) {
					errors.put("name", "name lenght min 10");
				}if(categoryDto.getName().length()>20) {
					errors.put("name", "name lenght max 100");
				}
			}
			
		// IsActive Validation
			if(ObjectUtils.isEmpty(categoryDto.getDescription())) {
				errors.put("description", "description cannot be null or empty");
			}
			
		// IsActive Validation
			if(ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				errors.put("IsActive", "IsActive cannot be null or empty");
		}else {
			if(!(categoryDto.getIsActive().equals(Boolean.TRUE) || categoryDto.getIsActive().equals(Boolean.FALSE))) {
				errors.put("IsActive","Invalid IsActive Value");
			}
		}
	}
		if(!errors.isEmpty()) {
			throw new validationException(errors);
		}		
}
	
	public void todoValidation(TodoDto todo) throws Exception {
		StatusDto reqStatus = todo.getStatus();
		Boolean statusFound = false;
		for (TodoStatus st : TodoStatus.values()) {
			if (st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		if (!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}

	}
		
}
