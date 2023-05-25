package com.babydev.app.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PersonalInformationDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private byte[] imageData;
}
