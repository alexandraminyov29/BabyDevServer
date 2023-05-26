package com.babydev.app.domain.dto.qualifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExperienceDTO {
	private short priority;
	private String title;
	private String companyName;
	private String position;
	private String dateFrom;
	private String dateTo;
	
	ExperienceDTO(){};
}
