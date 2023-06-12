package com.babydev.app.domain.dto;

import java.util.List;

import com.babydev.app.domain.dto.qualifications.EducationDTO;
import com.babydev.app.domain.dto.qualifications.ExperienceDTO;
import com.babydev.app.domain.dto.qualifications.SkillForCvDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private byte[] imageData;
	private List<EducationDTO> education;
	private List<ExperienceDTO> experience;
	private List<SkillForCvDTO> skill;
}
