package com.babydev.app.helper;

import org.springframework.beans.factory.annotation.Autowired;

import com.babydev.app.domain.entity.User;
import com.babydev.app.domain.entity.UserRole;
import com.babydev.app.security.config.JwtService;
import com.babydev.app.service.impl.UserService;

public class Permissions {
	
	@Autowired
	static
	UserService userService;
	@Autowired
	static
	JwtService jwtService;
	
	/**
	 *  Method that extracts the user from the token
	 *  
	 *  @param header the header that contains the email
	 */
	public static User extractUserFromToken(String header) {
		final String token = header.substring("Bearer ".length());
		final String emailAddress = jwtService.extractUsername(token);
		
		return userService.getUserByEmail(emailAddress);
	}
	
	/**
	 *  Method that checks if the user's role is admin
	 *  
	 *  @param userToCheck the user to be checked
	 */
	public static boolean isAdmin(final User userToCheck) {
		return userToCheck.getRole() == UserRole.ADMIN && isActive(userToCheck);
	}
	
	/**
	 *  Method that checks if the user's role is admin
	 *  
	 *  @param userToCheck the user to be checked
	 */
	public static boolean isRecruiter(final User userToCheck) {
		return userToCheck.getRole() == UserRole.RECRUITER && isActive(userToCheck);
	}
	
	public static boolean isActive(final User userToCheck) {
		return userToCheck.isActive();
	}
	
	/**
	 *  Method that checks the identity of the user with the given ID
	 *  
	 *  @param userToCheck the user to be checked
	 *  @param userId reference to the user id
	 */
	public static boolean isUserHimself(final User userToCheck, final long userId) {
		return userToCheck.getUserId() == userId;
	}
}
