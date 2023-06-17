package com.babydev.app.service.facade;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;

import java.util.List;

public interface JobServiceFacade {

    public List<Job> getJobs();

    public Job getJobById(Long id);

    public Job addJob(Job job, Long userId, Long companyId);

    public void deleteJob(Long id);

    public JobPageDTO findJobById(Long jobId, Long userId);

    public JobPageDTO getJobPageById(Long id);

  //  public List<JobListViewTypeDTO> sortAscJobsByDateOfPosting(List<JobListViewTypeDTO> jobs);

   // public List<JobListViewTypeDTO> sortDescJobsByDateOfPosting(List<JobListViewTypeDTO> jobs);

    public List<JobListViewTypeDTO> getJobsByLocation(String location);

    public List<JobListViewTypeDTO> getJobsByType(String jobType);

    public void applyJob(String token, Long jobId);

    public boolean addJobToFavorites(String token, Long jobId);

}
