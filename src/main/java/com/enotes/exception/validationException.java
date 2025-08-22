package com.enotes.exception;

import java.util.Map;

public class validationException extends RuntimeException {

	private Map<String, Object> errors;

	public validationException(Map<String, Object> errors) {
		super("invalid validation");
		this.errors = errors;
	}
	
	public Map <String,Object> getErrors(){
		return errors;
	}
}
