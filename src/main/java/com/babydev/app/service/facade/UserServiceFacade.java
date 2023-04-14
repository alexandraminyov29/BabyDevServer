package com.babydev.app.service.facade;

import org.springframework.lang.NonNull;

import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;

public interface UserServiceFacade {
	public void deleteUserById(@NonNull String authorizationHeader, @NonNull long id) throws NotAuthorizedException;

	User getUserByEmail(String email);
}
