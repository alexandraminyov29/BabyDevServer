package com.babydev.app.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babydev.app.domain.dto.qualifications.EducationDTO;
import com.babydev.app.domain.dto.qualifications.ExperienceDTO;
import com.babydev.app.domain.dto.qualifications.SkillDTO;
import com.babydev.app.domain.entity.Degree;
import com.babydev.app.domain.entity.Education;
import com.babydev.app.domain.entity.Experience;
import com.babydev.app.domain.entity.Skill;
import com.babydev.app.domain.entity.User;
import com.babydev.app.repository.qualifications.EducationRepository;
import com.babydev.app.repository.qualifications.ExperienceRepository;
import com.babydev.app.repository.qualifications.SkillRepository;

@Service
public class QualificationsService {
	
	@Autowired
	UserService userService;
	@Autowired
	JobService jobService;
	@Autowired
	SkillRepository skillRepository;
	@Autowired
	ExperienceRepository experienceRepository;
	@Autowired
	EducationRepository educationRepository;
	
	public EducationDTO addEducation(String authorizationHeader, EducationDTO education) {
		User user = userService.getUserFromToken(authorizationHeader);
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        LocalDate dateFrom = LocalDate.parse(education.getDateFrom(), formatter);
        LocalDate dateTo = LocalDate.parse(education.getDateTo(), formatter);
        
        Education newEducation = Education.builder()
				.priority(education.getPriority())
				.institution(education.getInstitution())
				.subject(education.getSubject())
				.dateFrom(dateFrom)
				.dateTo(dateTo)
				.user(user)
				.degree(Degree.valueOf(education.getDegree()))
				.build();
		user.getEducation().add(newEducation);
		userService.save(user);
		
		return education;
	}

	public SkillDTO addSkill(String authorizationHeader, SkillDTO skill) {
		User user = userService.getUserFromToken(authorizationHeader);
		
		Skill newSkill = Skill.builder()
				.priority(skill.getPriority())
				.skillName(skill.getSkillName())
				.skillExperience(skill.getSkillExperience())
				.user(user)
				.build();
		
		user.getSkills().add(newSkill);
		userService.save(user);
		
		return skill;
	}

	public ExperienceDTO addExperience(String authorizationHeader, ExperienceDTO experience) {
		User user = userService.getUserFromToken(authorizationHeader);	
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        LocalDate dateFrom = LocalDate.parse(experience.getDateFrom(), formatter);
        LocalDate dateTo = LocalDate.parse(experience.getDateTo(), formatter);
		
		Experience newExperience = Experience.builder()
				.priority(experience.getPriority())
				.title(experience.getTitle())
				.companyName(experience.getCompanyName())
				.position(experience.getPosition())
				.dateFrom(dateFrom)
				.dateTo(dateTo)
				.build();
		
		user.getExperience().add(newExperience);
		userService.save(user);
		
		return experience;
	}
}
