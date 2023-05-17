package com.babydev.app.service.impl;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.Location;
import com.babydev.app.domain.entity.User;
import com.babydev.app.repository.CompanyRepository;
import com.babydev.app.repository.JobRepository;
import com.babydev.app.repository.UserRepository;
import com.babydev.app.service.facade.JobServiceFacade;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobService implements JobServiceFacade {
    @Autowired
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).get();
    }

    public Job addJob(Job job, Long userId, Long companyId) {
        job.setAuthor(userRepository.findById(userId).get());
        job.setCompany(companyRepository.findCompanyByCompanyId(companyId).get());
        job.setPromotedUntil(LocalDateTime.of(1970, 12, 12, 10, 0));
        job.setPostDate(LocalDate.now());
        return jobRepository.save(job);
    }

    public List<JobListViewTypeDTO> searchJobs(String keyword) {
        return jobRepository.searchJobs(keyword);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public JobPageDTO findJobById(Long jobId, Long userId) {
        return jobRepository.findJobDetails(jobId, userId);
    }

//    public List<JobListViewTypeDTO> sortAscJobsByDateOfPosting(List<JobListViewTypeDTO> jobs) {
//        Collections.sort(jobs, new Comparator<JobListViewTypeDTO>() {
//            @Override
//            public int compare(JobListViewTypeDTO job1, JobListViewTypeDTO job2) {
//                if (job1.getPostedDate() == null)
//                    return 1;
//                else if (job2.getPostedDate() == null)
//                    return -1;
//                else
//                    return job1.getPostedDate().compareTo(job2.getPostedDate());
//            }
//        });
//        return jobs;
//    }

//    public List<JobListViewTypeDTO> sortDescJobsByDateOfPosting(List<JobListViewTypeDTO> jobs) {
//        jobs = sortAscJobsByDateOfPosting(jobs);
//        Collections.reverse(jobs);
//        return jobs;
//    }

    public List<JobListViewTypeDTO> sortByLocation(List<JobListViewTypeDTO> jobs, Location location) {
         Collections.sort(jobs, new Comparator<JobListViewTypeDTO>() {
             @Override
             public int compare(JobListViewTypeDTO j1, JobListViewTypeDTO j2) {
                 Location firstLocation = j1.getLocation();
                 Location secondLocation = j2.getLocation();

                 int j1Distance = Math.abs(firstLocation.ordinal() - location.ordinal());
                 int j2Distance = Math.abs(secondLocation.ordinal() - location.ordinal());

                 return Integer.compare(j1Distance,j2Distance);
             }
         });
         return jobs;
    }

    public List<JobListViewTypeDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        List<JobListViewTypeDTO> jobsDTO = new ArrayList<JobListViewTypeDTO>();
        for (Job job : jobs) {
            jobsDTO.add(new JobListViewTypeDTO(job));
        }

        return jobsDTO;
    }

    public void applyJob(Long userId, Long jobId){
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find user");
        }

        job.get().getApplicants().add(user.get());
        jobRepository.save(job.get());
    }

}
