package com.babydev.app.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterRequest {
	private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private CompanyInfoDTO company;
}
	