package com.babydev.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.babydev.app.domain.dto.qualifications.EducationDTO;
import com.babydev.app.domain.dto.qualifications.ExperienceDTO;
import com.babydev.app.domain.dto.qualifications.SkillDTO;
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
	
//	@GetMapping(value = "/education")
//	@Transactional
//	public ResponseEntity<?> getEducation (
//			@RequestHeader("Authorization") String authorizationHeader) {
//		return ResponseEntity.status(HttpStatus.OK).body(
//				qualificationsService.addEducation(authorizationHeader, education));
//	}
}
