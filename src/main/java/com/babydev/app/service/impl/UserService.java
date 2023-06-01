package com.babydev.app.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.dto.PersonalInformationDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.ImageUtil;
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

	public List<Job> getFavoriteJobs(String token) {
		User user = getUserFromToken(token);
		return user.getFavoriteJobs();
	}
	
	@Override
	public void deleteUserById(String authorizationHeader, long id) throws NotAuthorizedException {
		User user = getUserFromToken(authorizationHeader);
		if (!Permissions.isAdmin(user) || Permissions.isUserHimself(user, id)) {
			throw new NotAuthorizedException();
		}
		userRepository.deleteById(id);
	}
	
	@Override
    public void uploadImage(String authorizationHeader, byte[] array) throws IOException {
        User user = getUserFromToken(authorizationHeader);
        user.setImageData(ImageUtil.compressImage(array));
        
        save(user);
    }
	
	@Override
	public void updatePhoneNumber(String authorizationHeader, String phoneNumber) {
		User user = getUserFromToken(authorizationHeader);
		user.setPhoneNumber(phoneNumber);
		
		save(user);
	}
	
	public Object getMyPersonalInformationByTab(String token, int tab) {
		
		User user = getUserFromToken(token);
		switch (tab) {
			// Get all user info (NO TAB)
			case 0:
			// TODO get all info from all tabs
				break;
				
			// TAB 1 -> Personal Information
			case 1:
				return mapUserToPersonalInfo(user);
		}
		return null;
	}
	
	private PersonalInformationDTO mapUserToPersonalInfo(User user) {
		return PersonalInformationDTO.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.imageData(ImageUtil.decompressImage(user.getImageData()))
				.build();
	}

	public List<User> findAllActiveUsers() {
		List<User> users = userRepository.findAllByIsActive(true);
		return users;
	}
    
    public User save(User user) {
    	return userRepository.save(user);
    }
    
    public User getUserFromToken(String header) {
    	return getUserByEmail(jwtService.extractUsernameFromToken(header));
    }

	public boolean hasCv(String email) {
		return getUserByEmail(email).isHasCv();
	}
}
