package com.babydev.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babydev.app.domain.dto.PersonalInformationDTO;
import com.babydev.app.domain.dto.RecruiterRequest;
import com.babydev.app.domain.dto.RecruiterRequestListViewType;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.service.impl.UserService;

import jakarta.persistence.EntityNotFoundException;

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
	public ResponseEntity<?> updatePhoneNumber(@RequestHeader(value = "Authorization") String authorizationHeader,
			@RequestParam String newPhoneNumber) {
		userService.updatePhoneNumber(authorizationHeader, newPhoneNumber);
		return ResponseEntity.status(HttpStatus.OK).body(userService.updatePhoneNumber(authorizationHeader, newPhoneNumber));
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
	public ResponseEntity<PersonalInformationDTO> getMyPersonalInformation(
			@RequestHeader("Authorization") String authorizationHeader) {
		try {
			PersonalInformationDTO result = userService.getMyPersonalInformation(authorizationHeader);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	@GetMapping(value = "/profile/{id}")
	public ResponseEntity<PersonalInformationDTO> getPersonalInformationById(@RequestHeader("Authorization") String authorizationHeader,
			@PathVariable long id) {
		try {
			PersonalInformationDTO result = userService.getPersonalInformationById(authorizationHeader, id);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	@GetMapping(value = "/hasCv")
	public ResponseEntity<?> hasCv(@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam String email) {
		boolean result = userService.hasCv(email);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	@PostMapping(value ="/recruiters/request")
	public ResponseEntity<?> requestRecruiterAccount(@RequestBody RecruiterRequest request) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(userService.requestRecruiterAccount(request));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@GetMapping(value = "/recruiters/requests")
	public ResponseEntity<List<RecruiterRequestListViewType>> getRequestListData(
			@RequestHeader("Authorization") String authorizationHeader) {
		try {
			List<RecruiterRequestListViewType> result = userService.displayRequestListData(authorizationHeader);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	@GetMapping(value = "/recruiters/requests/view")
	public ResponseEntity<RecruiterRequest> getRequestData(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam String email) {
		try {
			RecruiterRequest result = userService.getRequestData(authorizationHeader, email);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@PatchMapping(value = "/recruiters/requests/resolve")
	public ResponseEntity<?> acceptRequestFromRecruiter(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam String email) {
		try {
			userService.acceptRequestFromRecruiter(authorizationHeader, email);
			return ResponseEntity.status(HttpStatus.OK).body("OK");
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
	
	@DeleteMapping(value = "/recruiters/requests/resolve")
	public ResponseEntity<?> deleteRequestFromRecruiter(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam String email) {
		try {
			userService.deleteRequestFromRecruiter(authorizationHeader, email);
			return ResponseEntity.status(HttpStatus.OK).body("OK");
		} catch (NotAuthorizedException e) {
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
