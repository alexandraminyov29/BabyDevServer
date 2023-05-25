package com.babydev.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.service.impl.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@DeleteMapping
	public ResponseEntity<Void> deleteUserById(@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam long id) {
		try {
			userService.deleteUserById(authorizationHeader, id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (EmptyResultDataAccessException e) {
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	@PatchMapping(value = "/image", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> updateImage(@RequestHeader("Authorization") String authorizationHeader, 
			@RequestBody byte[] array) {
		try {
			userService.uploadImage(authorizationHeader, array);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INSUFFICIENT_STORAGE);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
//	@PatchMapping(value = "/image")
//    public ResponseEntity<String> uploadImage(@RequestHeader("Authorization") String authorizationHeader, 
//    		@RequestParam("file") MultipartFile file) {
//        try {
//            byte[] imageData = file.getBytes();
//            userService.uploadImage(authorizationHeader, imageData);
//            return ResponseEntity.ok("Image uploaded successfully.");
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
//        }
//    }
	
	@PatchMapping(value = "/phoneno")
	public ResponseEntity<?> updatePhoneNumber(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody String newPhoneNumber) {
		userService.updatePhoneNumber(authorizationHeader, newPhoneNumber);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/myprofile")
	public ResponseEntity<?> getMyPersonalInformation(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam int tab) {
		Object result = userService.getMyPersonalInformationByTab(authorizationHeader, tab);
		if (result != null) {
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
