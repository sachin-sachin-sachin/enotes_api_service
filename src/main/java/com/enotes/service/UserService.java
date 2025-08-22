package com.enotes.service;

import com.enotes.dto.PasswordChngRequest;

public interface UserService {
	public void changePassword(PasswordChngRequest passwordRequest);
}
