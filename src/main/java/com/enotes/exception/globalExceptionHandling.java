package com.enotes.exception;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.enotes.util.commonUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class globalExceptionHandling {
	
	
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> ExceptionHandler(Exception e) {
//		log.error("globalExceptionHandling :: ExceptionHandler ::",e.getMessage());
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
		log.error("GlobalExceptionHandler :: handleException ::", e.getMessage());
		return commonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> nullPointerExceptionHandler(Exception e) {
		log.error("globalExceptionHandling :: nullPointerExceptionHandler ::",e.getMessage());
		return commonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(Exception e) {
		log.error("globalExceptionHandling :: resourceNotFoundExceptionHandler ::",e.getMessage());
		return commonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(validationException.class)
	public ResponseEntity<?> validationExceptionHandler(validationException e) {
		return commonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e) {
		return commonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
	}

	
	@ExceptionHandler(existDataException.class)
	public ResponseEntity<?> existDataExceptionHandler(existDataException e) {
		return commonUtil.createErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(AlreadyFavoritedException.class)
	public ResponseEntity<?> handleAlreadyFavorited(AlreadyFavoritedException e) {
	    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	   //         .body(Map.of("status", "Error", "message", ex.getMessage()));
	}
	
	
	
	
}
