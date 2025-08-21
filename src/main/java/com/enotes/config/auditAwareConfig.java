package com.enotes.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.enotes.entity.User;
import com.enotes.util.commonUtil;

public class auditAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {

		User loggedInUser = commonUtil.getLoggedInUser();
		return Optional.of(loggedInUser.getId());
	}

}
