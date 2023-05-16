package com.babydev.app.domain.dto;

import com.babydev.app.domain.entity.JobType;
import com.babydev.app.domain.entity.Location;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class JobPageDTO {

    private Long id;

    private String title;

    private String description;

    private Location location;

    private JobType type;

    private LocalDate postedDate;

    private String experienceRequired;

    private Boolean applied;

    private Long companyId;

    private String name;

    private byte[] image;

    public JobPageDTO() {
        super();
    }

    public JobPageDTO(java.lang.Long id, java.lang.String title, java.lang.String description, Location location, JobType type, LocalDate postedDate, java.lang.String experienceRequired, boolean applied, java.lang.Long companyId, java.lang.String name, byte[] image) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.postedDate = postedDate;
        this.experienceRequired = experienceRequired;
        this.applied = applied;
        this.companyId = companyId;
        this.name = name;
        this.image = image;
    }
}
