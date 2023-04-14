package com.babydev.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.Permissions;
import com.babydev.app.repository.UserRepository;
import com.babydev.app.security.config.JwtService;
import com.babydev.app.service.facade.UserServiceFacade;

@Service
public class UserService implements UserServiceFacade {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtService jwtService;

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).get();
	}
	@Override
	public void deleteUserById(String authorizationHeader, long id) throws NotAuthorizedException {
		User user = Permissions.extractUserFromToken(authorizationHeader);
		if (!Permissions.isAdmin(user) || Permissions.isUserHimself(user, id)) {
			throw new NotAuthorizedException();
		}
		userRepository.deleteById(id);
	}

}
