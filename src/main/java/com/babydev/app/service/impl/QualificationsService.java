package com.babydev.app.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.helper.Permissions;
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
        LocalDate dateTo;
        if (education.getDateTo() == null || education.getDateTo().isEmpty()) {
        	dateTo = null;
        } else {
        	dateTo = LocalDate.parse(education.getDateTo(), formatter);
        }
        
        Education newEducation = Education.builder()
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
        LocalDate dateTo;
        if (experience.getDateTo() == null || experience.getDateTo().isEmpty()) {
        	dateTo = null;
        } else {
        	dateTo = LocalDate.parse(experience.getDateTo(), formatter);
        }
		
		Experience newExperience = Experience.builder()
				.title(experience.getTitle())
				.companyName(experience.getCompanyName())
				.position(experience.getPosition())
				.dateFrom(dateFrom)
				.dateTo(dateTo)
				.user(user)
				.build();
		
		user.getExperience().add(newExperience);
		userService.save(user);
		
		return experience;
	}

	public List<EducationDTO> getEducationForUser(
			String authorizationHeader, String email) throws NotAuthorizedException {
		
		User user = getUserBasedOnAuthority(authorizationHeader, email);
		return mapEducationListToDTO(user.getEducation());
	}
	
	public List<SkillDTO> getSkillsForUser(
			String authorizationHeader, String email) throws NotAuthorizedException {
		
		User user = getUserBasedOnAuthority(authorizationHeader, email);
		return mapSkillListToDTO(user.getSkills());
	}
	
	public List<ExperienceDTO> getExperienceForUser(
			String authorizationHeader, String email) throws NotAuthorizedException {
		
		User user = getUserBasedOnAuthority(authorizationHeader, email);
		return mapExperienceListToDTO(user.getExperience());
	}
	
	public void deleteEducation(String authorizationHeader, String email, long id) throws NotAuthorizedException {
		canModifyQualification(authorizationHeader, email);
		educationRepository.deleteById(id);	
	}
	
	public void deleteSkill(String authorizationHeader, String email, long id) throws NotAuthorizedException {
		canModifyQualification(authorizationHeader, email);
		skillRepository.deleteById(id);	
	}
	
	public void deleteExperience(String authorizationHeader, String email, long id) throws NotAuthorizedException {
		canModifyQualification(authorizationHeader, email);
		experienceRepository.deleteById(id);	
	}
	
	public EducationDTO updateEducation(String authorizationHeader, String email, EducationDTO educationDTO) throws NotAuthorizedException {
		canModifyQualification(authorizationHeader, email);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		LocalDate dateFrom = LocalDate.parse(educationDTO.getDateFrom(), formatter);
        LocalDate dateTo;
        if (educationDTO.getDateTo() == null 
        		|| educationDTO.getDateTo().isEmpty() 
        		|| educationDTO.getDateTo().equals("actual")) {
        	dateTo = null;
        } else {
        	dateTo = LocalDate.parse(educationDTO.getDateTo(), formatter);
        }
        
        Education updatedEducation = educationRepository.findById(educationDTO.getId()).get();
        
        updatedEducation.setInstitution(educationDTO.getInstitution());
        updatedEducation.setSubject(educationDTO.getSubject());
        updatedEducation.setDegree(Degree.valueOf(educationDTO.getDegree()));
        updatedEducation.setDateFrom(dateFrom);
        updatedEducation.setDateTo(dateTo);
        
        educationRepository.save(updatedEducation);
		return educationDTO;
	}
	
	public SkillDTO updateSkill(String authorizationHeader, String email, SkillDTO skillDTO) throws NotAuthorizedException {
		canModifyQualification(authorizationHeader, email);
        
        Skill updatedSkill = skillRepository.findById(skillDTO.getId()).get();
        
        updatedSkill.setSkillName(skillDTO.getSkillName());
        updatedSkill.setSkillExperience(skillDTO.getSkillExperience());
        
        skillRepository.save(updatedSkill);
		return skillDTO;
	}
	
	public ExperienceDTO updateExperience(String authorizationHeader, String email, 
			ExperienceDTO experienceDTO) throws NotAuthorizedException {
		canModifyQualification(authorizationHeader, email);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		
		LocalDate dateFrom = LocalDate.parse(experienceDTO.getDateFrom(), formatter);
        LocalDate dateTo;
        if (experienceDTO.getDateTo() == null 
        		|| experienceDTO.getDateTo().isEmpty() 
        		|| experienceDTO.getDateTo().equals("actual")) {
        	dateTo = null;
        } else {
        	dateTo = LocalDate.parse(experienceDTO.getDateTo(), formatter);
        }
        
        Experience updatedExperience = experienceRepository.findById(experienceDTO.getId()).get();
        
        updatedExperience.setTitle(experienceDTO.getTitle());
        updatedExperience.setCompanyName(experienceDTO.getCompanyName());
        updatedExperience.setPosition(experienceDTO.getPosition());
        updatedExperience.setDateFrom(dateFrom);
        updatedExperience.setDateTo(dateTo);
        
        experienceRepository.save(updatedExperience);
		return experienceDTO;
	}
	
	private void canModifyQualification(final String token, final String email) throws NotAuthorizedException {
		User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) || !isStandardUser) {
			throw new NotAuthorizedException();
		}
	}
	
	private User getUserBasedOnAuthority(final String token, final String email) throws NotAuthorizedException {
		User user = userService.getUserFromToken(token);
		final boolean isStandardUser = Permissions.isStandard(user);
		if (!user.getEmail().equals(email) && isStandardUser) {
			throw new NotAuthorizedException();
		} else if (!isStandardUser) {
			user = userService.getUserByEmail(email);
		}
		return user;
	}
	
	private List<EducationDTO> mapEducationListToDTO (List<Education> elements) {
		List<EducationDTO> result = new ArrayList<EducationDTO>();
		
		for (Education e : elements.stream()
				.sorted(Comparator.comparing(Education::getDateTo, 
						Comparator.nullsFirst(Comparator.reverseOrder())))
				.toList()
				) {
			result.add(mapEducationToDTO(e));
		}
		return result;
	}
	
	private EducationDTO mapEducationToDTO (Education education) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        String dateFrom = education.getDateFrom().format(formatter);
        String dateTo;
        if (education.getDateTo() != null) {
        	dateTo = education.getDateTo().format(formatter);
        } else {
        	dateTo = "actual";
        }
        
		return EducationDTO.builder()
				.id(education.getEducationId())
				.institution(education.getInstitution())
				.subject(education.getSubject())
				.dateFrom(dateFrom)
				.dateTo(dateTo)
				.degree(education.getDegree().toString())
				.build();
	}
	
	private List<SkillDTO> mapSkillListToDTO (List<Skill> elements) {
		List<SkillDTO> result = new ArrayList<SkillDTO>();
		
		for (Skill e : elements.stream()
				.sorted(Comparator.comparing(Skill::getSkillExperience).reversed())
				.toList()
				) {
			result.add(mapSkillToDTO(e));
		}
		return result;
	}
	
	private SkillDTO mapSkillToDTO (Skill skill) {
        
		return SkillDTO.builder()
				.id(skill.getSkillId())
				.skillName(skill.getSkillName())
				.skillExperience(skill.getSkillExperience())
				.build();
	}
	
	private List<ExperienceDTO> mapExperienceListToDTO (List<Experience> elements) {
		List<ExperienceDTO> result = new ArrayList<ExperienceDTO>();
		
		for (Experience e : elements.stream()
				.sorted(Comparator.comparing(Experience::getDateTo, 
						Comparator.nullsFirst(Comparator.reverseOrder())))
				.toList()
				) {
			result.add(mapEducationToDTO(e));
		}
		return result;
	}
	
	private ExperienceDTO mapEducationToDTO (Experience experience) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        String dateFrom = experience.getDateFrom().format(formatter);
        String dateTo;
        if (experience.getDateTo() != null) {
        	dateTo = experience.getDateTo().format(formatter);
        } else {
        	dateTo = "actual";
        }
        
		return ExperienceDTO.builder()
				.id(experience.getExperienceId())
				.title(experience.getTitle())
				.companyName(experience.getCompanyName())
				.position(experience.getPosition())
				.dateFrom(dateFrom)
				.dateTo(dateTo)
				.build();
	}
}
