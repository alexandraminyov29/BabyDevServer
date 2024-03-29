package com.babydev.app.domain.dto.qualifications;

import com.babydev.app.domain.entity.ExperienceRating;

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
public class SkillDTO {
	private long id;
	private String skillName;
	private ExperienceRating skillExperience;
}
