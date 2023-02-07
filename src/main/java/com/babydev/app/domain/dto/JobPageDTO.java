package com.babydev.app.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JobPageDTO {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime postedDate;

    private String experienceRequired;

    private Boolean applied;

    private Long companyId;

    private String name;

    private byte[] image;

    public JobPageDTO() {
        super();
    }

    public JobPageDTO(java.lang.Long id, java.lang.String title, java.lang.String description, java.time.LocalDateTime postedDate, java.lang.String experienceRequired, java.lang.Long companyId, java.lang.String name, byte[] image) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.postedDate = postedDate;
        this.experienceRequired = experienceRequired;
        this.applied = false;
        this.companyId = companyId;
        this.name = name;
        this.image = image;
    }
}
