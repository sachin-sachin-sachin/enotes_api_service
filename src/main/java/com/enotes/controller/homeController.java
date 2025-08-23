package com.enotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.controllerEndpoint.HomeEndpoint;
import com.enotes.dto.PswdResetRequest;
import com.enotes.service.HomeService;
import com.enotes.service.UserService;
import com.enotes.util.commonUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class homeController implements HomeEndpoint{
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<?> verifyUserAccount(Integer uid,String code) throws Exception {
		Boolean verifyAccount = homeService.verifyAccount(uid, code);
		if (verifyAccount)
			return commonUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
		return commonUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<?> sendEmailForPasswordReset(String email, HttpServletRequest request)
			throws Exception {
		userService.sendEmailPasswordReset(email, request);
		return commonUtil.createBuildResponseMessage("Email Send Success !! Check Email Reset Password", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> verifyPasswordResetLink(Integer uid,String code)
			throws Exception {
		userService.verifyPswdResetLink(uid, code);
		return commonUtil.createBuildResponseMessage("verification success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> resetPassword(PswdResetRequest pswdResetRequest) throws Exception {
		userService.resetPassword(pswdResetRequest);
		return commonUtil.createBuildResponseMessage("Password reset succes", HttpStatus.OK);
	}
}
