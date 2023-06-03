package com.babydev.app.service.impl;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.JobType;
import com.babydev.app.domain.entity.Location;
import com.babydev.app.domain.entity.User;
import com.babydev.app.repository.CompanyRepository;
import com.babydev.app.repository.JobRepository;
import com.babydev.app.repository.UserRepository;
import com.babydev.app.security.config.JwtService;
import com.babydev.app.service.facade.JobServiceFacade;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService implements JobServiceFacade {
    @Autowired
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    private final JwtService jwtService;

    public List<Job> getJobs() {
//         = null;

        try {
            Process process = Runtime.getRuntime().exec("python3 /files/scripts/helloWorld.py");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String scriptOutput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public List<JobListViewTypeDTO> searchJobs(String keyword, String token) {
        String userId;
        if (token != "") {
            userId = jwtService.extractUserIdFromToken(token).toString();
        } else {
            userId = "";
        }

        return jobRepository.searchJobs(keyword, userId);
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

    public List<JobListViewTypeDTO> getJobsByLocation(String location) {
        List<JobListViewTypeDTO> jobs = getAllJobs();

        List<JobListViewTypeDTO> filteredJobs = jobs.stream()
                .filter(j -> j.getLocation() == Location.valueOf(location))
                .collect(Collectors.toList());
        if(filteredJobs.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find job");
        } else {
            return filteredJobs;
        }
    }

    public List<JobListViewTypeDTO> getJobsByType(String jobType) {
        List<JobListViewTypeDTO> jobs = getAllJobs();

        List<JobListViewTypeDTO> filteredJobs = jobs.stream()
                .filter(j -> j.getType() == JobType.valueOf(jobType))
                .collect(Collectors.toList());
        if(filteredJobs.isEmpty()) {
            throw new IllegalArgumentException("Couldn't find job");
        } else {
            return filteredJobs;
        }
    }

    public List<JobListViewTypeDTO> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        List<JobListViewTypeDTO> jobsDTO = new ArrayList<JobListViewTypeDTO>();
        for (Job job : jobs) {
            jobsDTO.add(new JobListViewTypeDTO(job));
        }

        return jobsDTO;
    }

    public void applyJob(String token, Long jobId) {
        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }
        Long userId = jwtService.extractUserIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find user");
        }
        job.get().getApplicants().add(user.get());
        jobRepository.save(job.get());
    }

    @Transactional
    public boolean addJobToFavorites(String token, Long jobId) {

        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }

        Long userId = jwtService.extractUserIdFromToken(token);
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find user");
        }
        boolean isFavorite = user.get().getFavoriteJobs().contains(job.get());

        if (isFavorite) {
            user.get().getFavoriteJobs().remove(job.get());
            job.get().getUsersFavorites().remove(user.get());
        } else {
            user.get().getFavoriteJobs().add(job.get());
            job.get().getUsersFavorites().add(user.get());
        }
        userRepository.save(user.get());
        jobRepository.save(job.get());

        return isFavorite;
    }

}
