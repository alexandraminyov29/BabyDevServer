package com.babydev.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babydev.app.domain.dto.qualifications.EducationDTO;
import com.babydev.app.domain.dto.qualifications.ExperienceDTO;
import com.babydev.app.domain.dto.qualifications.SkillDTO;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.service.impl.QualificationsService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/qualifications")
public class QualificationsController {
	@Autowired
	QualificationsService qualificationsService;
	
	@PostMapping(value = "/education")
	@Transactional
	public ResponseEntity<?> addEducationToUser (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestBody EducationDTO education) {
		return ResponseEntity.status(HttpStatus.OK).body(
				qualificationsService.addEducation(authorizationHeader, education));
	}
	
	@PostMapping(value = "/skill")
	@Transactional
	public ResponseEntity<?> addSkillToUser (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestBody SkillDTO skill) {
		return ResponseEntity.status(HttpStatus.OK).body(
				qualificationsService.addSkill(authorizationHeader, skill));
	}
	
	@PostMapping(value = "/experience")
	@Transactional
	public ResponseEntity<?> addExperienceToUser (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestBody ExperienceDTO experience) {
		return ResponseEntity.status(HttpStatus.OK).body(
				qualificationsService.addExperience(authorizationHeader, experience));
	}
	
	@GetMapping(value = "/education")
	@Transactional
	public ResponseEntity<?> getEducation (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					qualificationsService.getEducationForUser(authorizationHeader, email));
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@GetMapping(value = "/skill")
	@Transactional
	public ResponseEntity<?> getSkill (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					qualificationsService.getSkillsForUser(authorizationHeader, email));
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@GetMapping(value = "/experience")
	@Transactional
	public ResponseEntity<?> getExperience (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
					qualificationsService.getExperienceForUser(authorizationHeader, email));
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@DeleteMapping(value = "/education")
	public ResponseEntity<?> deleteEducation (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email,
			@RequestParam long id) {
		try {
			qualificationsService.deleteEducation(authorizationHeader, email, id);
			return ResponseEntity.status(HttpStatus.OK).body("DELETED");	
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@DeleteMapping(value = "/skill")
	public ResponseEntity<?> deleteSkill (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email,
			@RequestParam long id) {
		try {
			qualificationsService.deleteSkill(authorizationHeader, email, id);
			return ResponseEntity.status(HttpStatus.OK).body("DELETED");	
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@DeleteMapping(value = "/experience")
	public ResponseEntity<?> deleteExperience (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email,
			@RequestParam long id) {
		try {
			qualificationsService.deleteExperience(authorizationHeader, email, id);
			return ResponseEntity.status(HttpStatus.OK).body("DELETED");	
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@PutMapping("/education")
	public ResponseEntity<?> updateEducation (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email,
			@RequestBody EducationDTO educationDTO) {
		try {
			
			return ResponseEntity.status(HttpStatus.OK).body(qualificationsService.updateEducation(
					authorizationHeader, email, educationDTO));	
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@PutMapping("/skill")
	public ResponseEntity<?> updateSkill (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email,
			@RequestBody SkillDTO skillDTO) {
		try {
			
			return ResponseEntity.status(HttpStatus.OK).body(qualificationsService.updateSkill(
					authorizationHeader, email, skillDTO));	
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
	
	@PutMapping("/experience")
	public ResponseEntity<?> updateExperience (
			@RequestHeader("Authorization") String authorizationHeader, 
			@RequestParam String email,
			@RequestBody ExperienceDTO experienceDTO) {
		try {
			
			return ResponseEntity.status(HttpStatus.OK).body(qualificationsService.updateExperience(
					authorizationHeader, email, experienceDTO));	
		} catch (NotAuthorizedException e) {
			return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body("METHOD NOT ALLOWED");
		}
	}
}
