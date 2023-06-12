package com.babydev.app.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.babydev.app.domain.dto.qualifications.EducationDTO;
import com.babydev.app.domain.dto.qualifications.ExperienceDTO;
import com.babydev.app.domain.dto.qualifications.SkillDTO;
import com.babydev.app.domain.dto.qualifications.SkillForCvDTO;
import com.babydev.app.domain.entity.DevelopmentSkill;
import com.babydev.app.domain.entity.Education;
import com.babydev.app.domain.entity.Experience;
import com.babydev.app.domain.entity.Skill;

public class FormatUtil {
	
	// used for jobs
	public static String formatPostDateToString(LocalDate date) {
    	LocalDate currentDate = LocalDate.now();
        long daysDiff = ChronoUnit.DAYS.between(date, currentDate);

        if (daysDiff < 1) {
            long minutesDifference = ChronoUnit.MINUTES.between(date.atStartOfDay(), currentDate.atStartOfDay());
            if (minutesDifference == 0) {
            	return "Just now";
            } else if (minutesDifference == 1) {
            	return "One minute ago";
            }
            return minutesDifference + " minutes ago";
        } else if (daysDiff < 7) {
            long hoursDiff = ChronoUnit.HOURS.between(date.atStartOfDay(), currentDate.atStartOfDay());
            if (hoursDiff == 1) {
            	return "One hour ago";
            }
            return hoursDiff + " hours ago";
        } else if (daysDiff <= 7) {
        	if (daysDiff == 1) {
        		return "Yesterday";
        	}
            return daysDiff + " days ago";
        } else {
            return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
    }
	
	public static DevelopmentSkill getSkillFromEnumValue(String value) {
	    for (DevelopmentSkill skill : DevelopmentSkill.values()) {
	        if (skill.getDisplayName().equalsIgnoreCase(value)) {
	            return skill;
	        }
	    }
	    return null; 
	}
	
	public static EducationDTO mapEducationToDTO (Education education) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        String dateFrom = education.getDateFrom().format(formatter);
        String dateTo;
        if (education.getDateTo() != null) {
        	dateTo = education.getDateTo().format(formatter);
        } else {
        	dateTo = "until present";
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
	
	public static ExperienceDTO mapExperienceToDTO (Experience experience) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        String dateFrom = experience.getDateFrom().format(formatter);
        String dateTo;
        if (experience.getDateTo() != null) {
        	dateTo = experience.getDateTo().format(formatter);
        } else {
        	dateTo = "until present";
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
	
	public static SkillDTO mapSkillToDTO (Skill skill) {
        
		return SkillDTO.builder()
				.id(skill.getSkillId())
				.skillName(skill.getSkillName().getDisplayName())
				.skillExperience(skill.getSkillExperience())
				.build();
	}
	
	public static SkillForCvDTO mapSkillToDTOForCv (Skill skill) {
        
		return SkillForCvDTO.builder()
				.id(skill.getSkillId())
				.skillName(skill.getSkillName().getDisplayName())
				.skillExperience(convertSkillLevelToString(skill.getSkillExperience().ordinal()))
				.build();
	}
	
	public static String convertSkillLevelToString (int ordinal) {
		switch (ordinal) {
		case 0: return "Novice";
		case 1: return "Beginner";
		case 2: return "Intermediate";
		case 3: return "Advanced";
		case 4: return "Expert";
		}
		return null;
	}
}
