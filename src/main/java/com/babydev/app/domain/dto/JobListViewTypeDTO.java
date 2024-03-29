package com.babydev.app.domain.dto;

import com.babydev.app.domain.entity.Company;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.JobType;
import com.babydev.app.domain.entity.Location;
import com.babydev.app.helper.FormatUtil;
import com.babydev.app.helper.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class JobListViewTypeDTO {

    private Long id;

    private String title;

    private Location location;

    private JobType type;

    private boolean isFavorite;

    private String postedDate;

    private String experienceRequired;

    private Long companyId;

    private String name;

    private byte[] image;

    private int score;

    public JobListViewTypeDTO() {}

    public JobListViewTypeDTO(Job job) {
        this.id = job.getJobId();
        this.title = job.getTitle();
        this.location = job.getLocation();
        this.type = job.getType();
        this.postedDate = FormatUtil.formatPostDateToString(job.getPostDate());
        this.experienceRequired = job.getExperienceRequired();

        final Company company = job.getCompany();

        this.companyId = company.getCompanyId();
        this.name = company.getName();
        this.image = company.getImage() != null ? ImageUtil.decompressImage(company.getImage()) : null;


    }

    public JobListViewTypeDTO(Long id, String title, Location location, LocalDate postedDate, JobType type, boolean isFavorite, String experienceRequired, Long companyId, String name, byte[] image) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.type = type;
        this.isFavorite = isFavorite;
        this.postedDate = FormatUtil.formatPostDateToString(postedDate);
        this.experienceRequired = experienceRequired;
        this.companyId = companyId;
        this.name = name;
        this.image = image != null ? ImageUtil.decompressImage(image) : null;
    }
}
