package com.babydev.app.service.facade;

import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface UserServiceFacade {
	public void deleteUserById(@NonNull String authorizationHeader, @NonNull long id) throws NotAuthorizedException;

	User getUserByEmail(String email);
	
	void uploadImage(@NonNull String authorizationHeader, byte[] array) throws IOException;
	
	String updatePhoneNumber(String authorizationHeader, String phoneNumber);

}
