package com.enotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class globalExceptionHandling {
	
	
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<?> ExceptionHandler(Exception e) {
//		log.error("globalExceptionHandling :: ExceptionHandler ::",e.getMessage());
//		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
//	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> nullPointerExceptionHandler(Exception e) {
		log.error("globalExceptionHandling :: nullPointerExceptionHandler ::",e.getMessage());
		return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundExceptionHandler(Exception e) {
		log.error("globalExceptionHandling :: resourceNotFoundExceptionHandler ::",e.getMessage());
		return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(validationException.class)
	public ResponseEntity<?> validationExceptionHandler(validationException e) {
		return new ResponseEntity<>(e.getErrors(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(existDataException.class)
	public ResponseEntity<?> existDataExceptionHandler(existDataException e) {
		return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
	}
	
	
	
	
	
}
