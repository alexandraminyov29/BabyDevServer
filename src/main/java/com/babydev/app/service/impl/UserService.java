package com.babydev.app.service.impl;

import com.babydev.app.domain.dto.*;
import com.babydev.app.domain.entity.*;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.FormatUtil;
import com.babydev.app.helper.ImageUtil;
import com.babydev.app.helper.Permissions;
import com.babydev.app.repository.UserRepository;
import com.babydev.app.security.config.JwtService;
import com.babydev.app.service.facade.UserServiceFacade;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceFacade {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	JwtService jwtService;
	@Autowired
	CompanyService companyService;
	@Autowired
	PasswordEncoder passwordEncoder;

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
	
	@Transactional
	public RecruiterRequest requestRecruiterAccount(RecruiterRequest request) throws EntityNotFoundException {
		
		CompanyInfoDTO companyInfo = request.getCompany();
		Company company;
		byte[] companyImage;
		
		if (companyInfo.isExistent()) {
			company = companyService.getCompanyByName(companyInfo.getName());
			companyImage = ImageUtil.decompressImage(company.getImage());
		} else {
			companyImage = ImageUtil.compressImage(Base64.getDecoder().decode(companyInfo.getImage()));
			company = Company.builder()
					.name(companyInfo.getName())
					.image(companyImage)
					.webPage(companyInfo.getWebPage())
					.build();
		}
		
		User user = User.builder()
	            .firstName(request.getFirstName())
	            .lastName(request.getLastName())
	            .email(request.getEmail())
	            .phoneNumber(request.getPhoneNumber())
	            .password(passwordEncoder.encode(request.getPassword()))
	            .role(UserRole.RECRUITER)
	            .company(company)
	            .isActive(false)
	            .imageData(companyImage)
	            .build();
		
		userRepository.save(user);
		company.getRecruiters().add(user);
		companyService.save(company);
		
		return request;
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
		return getUserByEmail(email).getHasCv();
	}

	public List<RecruiterRequestListViewType> displayRequestListData(String authorizationHeader)
			throws NotAuthorizedException {
		final User user = getUserFromToken(authorizationHeader);
		if (!Permissions.isAdmin(user)) {
			throw new NotAuthorizedException();
		}

		final List<User> recruiters = userRepository.findAllByRoleAndIsActive(UserRole.RECRUITER, false);
		List<RecruiterRequestListViewType> result = new ArrayList<RecruiterRequestListViewType>();
		
		for (User recruiter : recruiters) {
			result.add(
					RecruiterRequestListViewType.builder()
					.firstName(recruiter.getFirstName())
					.lastName(recruiter.getLastName())
					.email(recruiter.getEmail())
					.companyName(recruiter.getCompany().getName())
					.build()
					);
		}

		return result;
	}

	public RecruiterRequest getRequestData(String authorizationHeader, String email) throws NotAuthorizedException {
		final User user = getUserFromToken(authorizationHeader);
		if (!Permissions.isAdmin(user)) {
			throw new NotAuthorizedException();
		}
		
		final User userData = userRepository.findByEmail(email).orElseThrow();
		final Company userCompany = userData.getCompany();
		return RecruiterRequest.builder()
				.firstName(userData.getFirstName())
				.lastName(userData.getLastName())
				.email(userData.getEmail())
				.phoneNumber(userData.getPhoneNumber())
				.company(CompanyInfoDTO.builder()
						.name(userCompany.getName())
						.webPage(userCompany.getWebPage())
						.imageData(ImageUtil.decompressImage(userCompany.getImage()))
						.build())
				.build();
	}

	public void acceptRequestFromRecruiter(String authorizationHeader, String email) throws NotAuthorizedException {
		final User user = getUserFromToken(authorizationHeader);
		if (!Permissions.isAdmin(user)) {
			throw new NotAuthorizedException();
		}
		
		final User recruiter = userRepository.findByEmail(email).orElseThrow();
		recruiter.setIsActive(true);
		userRepository.save(recruiter);
	}

	public void deleteRequestFromRecruiter(String authorizationHeader, String email) throws NotAuthorizedException {
		final User user = getUserFromToken(authorizationHeader);
		if (!Permissions.isAdmin(user)) {
			throw new NotAuthorizedException();
		}
		
		final User recruiter = userRepository.findByEmail(email).orElseThrow();
		Company company = recruiter.getCompany();
		final boolean isLastRecruiter = company.getRecruiters().size() == 1;
		
		userRepository.deleteById(recruiter.getUserId());
		
		if (isLastRecruiter) {
			companyService.deleteCompany(company.getCompanyId());
		}
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
}
