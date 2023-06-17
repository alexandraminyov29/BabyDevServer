package com.babydev.app.domain.dto;

import com.babydev.app.domain.entity.Company;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.JobType;
import com.babydev.app.domain.entity.Location;
import com.babydev.app.helper.FormatUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JobPageDTO {

    private Long id;

    private String title;

    private String description;

    private Location location;

    private JobType type;

    private String postedDate;

    private String experienceRequired;

    private Long companyId;

    private String name;

    private byte[] image;

    public JobPageDTO() {
        super();
    }

    public JobPageDTO(Job job) {
        this.id = job.getJobId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.type = job.getType();
        this.postedDate = FormatUtil.formatPostDateToString(job.getPostDate());
        this.experienceRequired = job.getExperienceRequired();

        // this.isPromoted = job.getPromotedUntil().compareTo(LocalDateTime.now()) > 0;

        final Company company = job.getCompany();

        this.companyId = company.getCompanyId();
        this.name = company.getName();
        this.image = company.getImage() != null ? company.getImage() : new byte[1];


    }
    public JobPageDTO(java.lang.Long id, java.lang.String title, java.lang.String description, Location location, JobType type, String postedDate, java.lang.String experienceRequired, java.lang.Long companyId, java.lang.String name, byte[] image) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.postedDate = postedDate;
        this.experienceRequired = experienceRequired;
        this.companyId = companyId;
        this.name = name;
        this.image = image;
    }
}
