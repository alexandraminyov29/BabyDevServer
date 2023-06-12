package com.babydev.app.domain.dto;

import com.babydev.app.domain.entity.Location;
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
	private Location location;
	private byte[] imageData;
}
