package com.babydev.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.babydev.app.domain.entity.CurriculumVitae;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.service.impl.CVService;
import com.babydev.app.service.impl.FileStorageService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/cv")
public class CVController {

    @Autowired
    private CVService cvService;
    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCV(@RequestHeader("Authorization") String authorizationHeader, 
    		@RequestParam("file") MultipartFile file,
    		@RequestParam("email") String email) {
    	
        CurriculumVitae storedCV;
		try {
			storedCV = cvService.storeCV(authorizationHeader, file, email);
			return ResponseEntity.ok("CV uploaded successfully. CV ID: " + storedCV.getId());
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
		}
		
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadCV(@RequestHeader("Authorization") String authorizationHeader,
    		@RequestParam String email) {
    	
    		try {
				CurriculumVitae cv = cvService.getCVByUserEmail(authorizationHeader, email);
	            String fileName = cv.getFileName();

	            byte[] fileContent;
	            
				fileContent = fileStorageService.getFileContent(fileName);
				HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_PDF);
	            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fileName).build());

	            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    		} catch (NotAuthorizedException e) {
    			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
    		} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
			}
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCV(@RequestHeader("Authorization") String authorizationHeader,
    		@RequestParam String email) {
        try {
			cvService.deleteCV(authorizationHeader, email);
			return ResponseEntity.ok("CV deleted successfully.");
        } catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
        
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateCV(@PathVariable Long userId,
                                           @RequestParam("file") MultipartFile file) {
    	CurriculumVitae updatedCV;
		try {
			updatedCV = cvService.updateCV(userId, file);
	        return ResponseEntity.ok("CV updated successfully. CV ID: " + updatedCV.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.notFound().build();
    }
}

