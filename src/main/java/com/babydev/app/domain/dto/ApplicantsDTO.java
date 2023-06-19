package com.babydev.app.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantsDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String location;

}
