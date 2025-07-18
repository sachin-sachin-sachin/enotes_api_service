package com.enotes.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class projectConfig {

	@Bean
	public  ModelMapper modelmapper() {
		return new ModelMapper();

	}

}
