package com.enotes.service;

import com.enotes.dto.UserDto;

public interface UserService {

	Boolean register(UserDto userDto, String url) throws Exception;
}
