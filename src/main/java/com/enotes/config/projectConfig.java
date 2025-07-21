package com.enotes.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class projectConfig {

	@Bean
	public  ModelMapper modelmapper() {
		return new ModelMapper();
	}
	
	@Bean
	public AuditorAware<Integer> auditAware(){
		return new auditAwareConfig();
	}

}
