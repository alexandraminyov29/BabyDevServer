package com.babydev.app.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.babydev.app.domain.entity.CurriculumVitae;
import com.babydev.app.domain.entity.User;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.Permissions;
import com.babydev.app.repository.CVRepository;

import jakarta.transaction.Transactional;

@Service
public class CVService {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private UserService userService;

    @Transactional
    public CurriculumVitae storeCV(String token, MultipartFile file, String email) throws Exception {
    	User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) || !isStandardUser) {
			throw new NotAuthorizedException();
		}
		
        String fileName = fileStorageService.storeFile(file);

        CurriculumVitae cv = CurriculumVitae.builder()
        		.id(user.getUserId())
        		.user(user)
        		.fileName(fileName)
        		.build();
        
        user.setHasCv(true);
        userService.save(user);
        
        return cvRepository.save(cv);
    }

    public Optional<CurriculumVitae> getCVById(Long id) {
        return cvRepository.findById(id);
    }

    @Transactional
    public void deleteCV(String token, String email) throws NotAuthorizedException {
    	
    	User user = userService.getUserFromToken(token);
    	final boolean isAdmin = Permissions.isAdmin(user);
    	if (!user.getEmail().equals(email) && !isAdmin) {
			throw new NotAuthorizedException();
		}
    	
    	User userToBeUpdated = userService.getUserByEmail(email);
		CurriculumVitae cv = getCVById(userToBeUpdated.getUserId()).get();
		
		userToBeUpdated.setHasCv(false);
		
		userService.save(userToBeUpdated);
		cvRepository.delete(cv);
		
		try {
			fileStorageService.deleteFile(cv.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public CurriculumVitae updateCV(Long userId, MultipartFile file) throws Exception {
        // Retrieve the CV by user ID
        Optional<CurriculumVitae> cvOptional = cvRepository.findById(userId);

        // Update the CV if it exists
        if (cvOptional.isPresent()) {
        	CurriculumVitae cv = cvOptional.get();

            // Delete the existing file
            fileStorageService.deleteFile(cv.getFileName());

            // Store the new file and update the file name
            String fileName = fileStorageService.storeFile(file);
            cv.setFileName(fileName);

            // Save the updated CV in the database
            return cvRepository.save(cv);
        }
        else return null;
    }

	public CurriculumVitae getCVByUserEmail(String token, String email) throws NotAuthorizedException {
		User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) && isStandardUser) {
			throw new NotAuthorizedException();
		}
		return getCVById(userService.getUserByEmail(email).getUserId()).get();
	}
}

