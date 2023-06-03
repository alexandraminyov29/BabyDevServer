package com.babydev.app.domain.dto.qualifications;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.babydev.app.domain.entity.DevelopmentSkill;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSkillsDTO {
	private List<SkillDTO> skills;
	private List<String> skillNames;
	
	public UserSkillsDTO(final List<SkillDTO> skills) {
		this.skillNames = Arrays.stream(DevelopmentSkill.values())
	            .map(DevelopmentSkill::getDisplayName)
	            .collect(Collectors.toList());
		this.skills = skills;
	}
}
