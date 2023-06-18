package com.babydev.app.service.facade;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.exception.NotAuthorizedException;

import java.util.List;

public interface JobServiceFacade {

    public List<Job> getJobs();

    public Job getJobById(Long id);

    public void addJob(String token, JobPageDTO jobPageDT) throws NotAuthorizedException;

    public void editJob(String token, Job jobPageDTO, Long jobId) throws NotAuthorizedException;

    public void deleteJob(Long id);

    public JobPageDTO findJobById(Long jobId, Long userId);

    public JobPageDTO getJobPageById(String token, Long id);

  //  public List<JobListViewTypeDTO> sortAscJobsByDateOfPosting(List<JobListViewTypeDTO> jobs);

   // public List<JobListViewTypeDTO> sortDescJobsByDateOfPosting(List<JobListViewTypeDTO> jobs);

    public List<JobListViewTypeDTO> getJobsByLocation(String token, String location);

    public List<JobListViewTypeDTO> getJobsByType(String token, String jobType);

    public void applyJob(String token, Long jobId);

    public boolean addJobToFavorites(String token, Long jobId);

}
