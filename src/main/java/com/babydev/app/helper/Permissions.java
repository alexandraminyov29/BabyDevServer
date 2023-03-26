package com.babydev.app.helper;

import com.babydev.app.domain.entity.User;
import com.babydev.app.domain.entity.UserRole;

public class Permissions {
	
	/**
	 *  Method that checks if the user's role is admin
	 *  
	 *  @param userToCheck the user to be checked
	 */
	public static boolean isAdmin(final User userToCheck) {
		return userToCheck.getRole() == UserRole.ADMIN;
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
