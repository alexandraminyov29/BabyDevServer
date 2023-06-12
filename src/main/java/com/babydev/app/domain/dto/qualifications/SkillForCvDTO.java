package com.babydev.app.domain.dto.qualifications;

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
public class SkillForCvDTO {
	private long id;
	private String skillName;
	private String skillExperience;
}