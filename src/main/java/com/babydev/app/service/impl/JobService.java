package com.babydev.app.service.impl;

import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    @Autowired
    private final JobRepository jobRepository;

    public List<Job> getJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).get();
    }

    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public JobPageDTO findJobById(Long jobId, Long userId) {
        return jobRepository.findJobDetails(jobId);
    }
}
