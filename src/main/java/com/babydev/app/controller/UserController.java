package com.babydev.app.controller;

import com.babydev.app.domain.entity.Job;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.service.impl.UserService;
import jakarta.persistence.EntityNotFoundException;
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

import java.io.IOException;
import java.util.List;

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
	
	@PatchMapping(value = "/phoneno")
	public ResponseEntity<?> updatePhoneNumber(@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody String newPhoneNumber) {
		userService.updatePhoneNumber(authorizationHeader, newPhoneNumber);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	@GetMapping("/favorite")
	public ResponseEntity<?> getUserFavorites(@RequestHeader(value = "Authorization") String authorizationHeader) {
		try {
			List<Job> favoriteJobs = userService.getFavoriteJobs(authorizationHeader);
			return ResponseEntity.status(HttpStatus.OK).body(favoriteJobs);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
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
	
	@GetMapping(value = "/hasCv")
	public ResponseEntity<?> hasCv(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam String email) {
		boolean result = userService.hasCv(email);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
