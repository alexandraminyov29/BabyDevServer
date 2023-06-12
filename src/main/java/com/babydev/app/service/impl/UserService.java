package com.babydev.app.service.impl;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.dto.PersonalInformationDTO;
import com.babydev.app.domain.dto.UserInfoDTO;
import com.babydev.app.domain.entity.Education;
import com.babydev.app.domain.entity.Experience;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.Skill;
import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.FormatUtil;
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
	public String updatePhoneNumber(String authorizationHeader, String phoneNumber) {
		User user = getUserFromToken(authorizationHeader);
		user.setPhoneNumber(phoneNumber);
		
		save(user);
		return phoneNumber;
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
				.location(user.getLocation())
				.imageData(ImageUtil.decompressImage(user.getImageData()))
				.build();
	}
	
	// Used for resume generation
	public UserInfoDTO getUserInfo(User user) {
		return UserInfoDTO.builder()
				// personal information
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.phoneNumber(user.getPhoneNumber())
				.imageData(ImageUtil.decompressImage(user.getImageData()))
				// education
				.education(user.getEducation().stream()
				        .sorted(Comparator.comparing(Education::getDateTo, Comparator.nullsFirst(Comparator.reverseOrder())))
				        .map(FormatUtil::mapEducationToDTO)
				        .collect(Collectors.toList()))
				// experience
				.experience(user.getExperience().stream()
				        .sorted(Comparator.comparing(Experience::getDateTo, Comparator.nullsFirst(Comparator.reverseOrder())))
				        .map(FormatUtil::mapExperienceToDTO)
				        .collect(Collectors.toList()))
				// skills
				.skill(user.getSkills().stream()
						.sorted(Comparator.comparing(Skill::getSkillExperience).reversed())
						.map(FormatUtil::mapSkillToDTOForCv)
						.collect(Collectors.toList()))
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
