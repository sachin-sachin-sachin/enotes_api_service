package com.enotes.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.controllerEndpoint.UserEndpoint;
import com.enotes.dto.PasswordChngRequest;
import com.enotes.dto.UserResponse;
import com.enotes.entity.User;
import com.enotes.service.AuthService;
import com.enotes.service.UserService;
import com.enotes.util.commonUtil;

@RestController
public class UserController implements UserEndpoint {

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private UserService userService;

	
	@Override
	public ResponseEntity<?> getProfile()
	{
		User loggedInUser = commonUtil.getLoggedInUser();
		UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);
		return commonUtil.createBuildResponse(userResponse, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordRequest) {
		userService.changePassword(passwordRequest);
		return commonUtil.createBuildResponseMessage("Password change success", HttpStatus.OK);
	}

}
