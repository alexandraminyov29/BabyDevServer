package com.babydev.app.domain.dto;

import java.time.LocalDate;

import com.babydev.app.domain.entity.Company;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.JobType;
import com.babydev.app.domain.entity.Location;
import com.babydev.app.helper.FormatUtil;
import com.babydev.app.helper.ImageUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPageDTO {

    private Long id;

    private String title;

    private String description;

    private Location location;

    private JobType type;

    private String postedDate;

    private String experienceRequired;
    
    private boolean hasApplied;

    private Long companyId;

    private String name;
    
    private String link; 
    
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
        this.description = job.getDescription();
        // this.isPromoted = job.getPromotedUntil().compareTo(LocalDateTime.now()) > 0;

        final Company company = job.getCompany();
        
        this.link = company.getWebPage();
        this.companyId = company.getCompanyId();
        this.name = company.getName();
        this.image = company.getImage() != null ? ImageUtil.decompressImage(company.getImage()) : null;
    }
    
    public JobPageDTO(Long id, String title, String description, Location location, JobType type, LocalDate postedDate, String experienceRequired, Boolean hasApplied, Long companyId, String name, String webPage, byte[] image) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.postedDate = FormatUtil.formatPostDateToString(postedDate);
        this.experienceRequired = experienceRequired;
        this.hasApplied = hasApplied;
        this.companyId = companyId;
        this.name = name;
        this.link = webPage;
        this.image = image != null ? ImageUtil.decompressImage(image) : null;
    }
}
