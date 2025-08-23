package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.controllerEndpoint.AuthEndpoint;
import com.enotes.dto.LoginRequest;
import com.enotes.dto.LoginResponse;
import com.enotes.dto.UserRequest;
import com.enotes.service.AuthService;
import com.enotes.util.commonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthController implements AuthEndpoint{
	@Autowired
	private AuthService authService;

	@Override
	public ResponseEntity<?> registerUser(UserRequest userRequest,HttpServletRequest request) throws Exception {
		String url=commonUtil.getUrl(request);
		Boolean register = authService.register(userRequest,url);
		if (register) {
			return commonUtil.createBuildResponseMessage("Register success", HttpStatus.CREATED);
		}
		return commonUtil.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@Override
	public ResponseEntity<?> login(LoginRequest loginRequest) throws Exception {

		LoginResponse loginResponse = authService.login(loginRequest);
		if (ObjectUtils.isEmpty(loginResponse)) {
			return commonUtil.createErrorResponseMessage("invalid credential", HttpStatus.BAD_REQUEST);
		}
		return commonUtil.createBuildResponse(loginResponse,HttpStatus.OK);
	}
}
