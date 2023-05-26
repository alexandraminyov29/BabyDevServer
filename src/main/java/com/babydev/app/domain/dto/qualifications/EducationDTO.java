package com.babydev.app.domain.dto.qualifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EducationDTO {
	private short priority;
    private String institution;
    private String subject;
    private String dateFrom;
    private String dateTo;
    private String degree;
    
    EducationDTO(){}
}
