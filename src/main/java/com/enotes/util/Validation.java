package com.enotes.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.enotes.repository.RoleRepository;
import com.enotes.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.enotes.dto.CategoryDto;
import com.enotes.dto.TodoDto;
import com.enotes.dto.TodoDto.StatusDto;
import com.enotes.dto.UserRequest;
import com.enotes.enums.TodoStatus;
import com.enotes.exception.ResourceNotFoundException;
import com.enotes.exception.existDataException;
import com.enotes.exception.validationException;

@Component
public class Validation {
	

	@Autowired
	private RoleRepository roleRepo;
	

	@Autowired
	private UserRepository userRepo;

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
	
	public void userValidation(UserRequest userRequest) {

		if (!StringUtils.hasText(userRequest.getFirstName())) {
			throw new IllegalArgumentException("first name is invalid");
		}

		if (!StringUtils.hasText(userRequest.getLastName())) {
			throw new IllegalArgumentException("last name is invalid");
		}

		if (!StringUtils.hasText(userRequest.getEmail()) || !userRequest.getEmail().matches(Constants.EMAIL_REGEX)) {
			throw new IllegalArgumentException("email is invalid");
		} else {
			// validate email exist
			Boolean existEmail = userRepo.existsByEmail(userRequest.getEmail());
			if (existEmail) {
				throw new existDataException("Email already exist");
			}
		}

		if (!StringUtils.hasText(userRequest.getMobNo()) || !userRequest.getMobNo().matches(Constants.MOBNO_REGEX)) {
			throw new IllegalArgumentException("mobno is invalid");
		}

		if (CollectionUtils.isEmpty(userRequest.getRoles())) {
			throw new IllegalArgumentException("role is invalid");
		} else {

			List<Integer> roleIds = roleRepo.findAll().stream().map(r -> r.getId()).toList();

			List<Integer> invalidReqRoleids = userRequest.getRoles().stream().map(r -> r.getId())
					.filter(roleId -> !roleIds.contains(roleId)).toList();

			if (!CollectionUtils.isEmpty(invalidReqRoleids)) {
				throw new IllegalArgumentException("role is invalid" + invalidReqRoleids);
			}

		}

	}
		
}
