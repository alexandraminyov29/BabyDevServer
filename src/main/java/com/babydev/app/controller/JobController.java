package com.babydev.app.controller;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.exception.NotAuthorizedException;
import com.babydev.app.service.impl.JobService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    @Autowired
    private JobService jobService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable("id") Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).body("Job with id" + id + "was deleted.");
    }

    @GetMapping
    public ResponseEntity<JobPageDTO> getJobById(@RequestParam Long jobId, @RequestParam Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.findJobById(jobId, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobListViewTypeDTO>> getJobs(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs(authorizationHeader));
    }

    @GetMapping("/rall")
    public ResponseEntity<?> getRecruiterJobs(@RequestHeader(value = "Authorization") String authorizationHeader) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllRecruiterJobs(authorizationHeader));
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    @GetMapping("/getApplicants")
    public ResponseEntity<?> getApplicants(@RequestHeader(value = "Authorization") String authorizationHeader,
                                           @RequestParam Long jobId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(jobService.getApplicants(authorizationHeader, jobId));
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<JobListViewTypeDTO>> getFavoriteJobs(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getFavoriteJobs(authorizationHeader));
    }

    @GetMapping("/applied")
    public ResponseEntity<List<JobListViewTypeDTO>> getAppliedJobs(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAppliedJobs(authorizationHeader));
    }

    @GetMapping("/recommend")
    public ResponseEntity<List<JobListViewTypeDTO>> recommendJobs(@RequestHeader(value = "Authorization") String authorizationHeader) {
            return ResponseEntity.status(HttpStatus.OK).body(jobService.sortByScore(authorizationHeader));

    }

    @GetMapping("/search")
    public ResponseEntity<List<JobListViewTypeDTO>> searchJobs(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                               @RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.searchJobs(keyword, authorizationHeader));
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyToJob(@RequestHeader(value = "Authorization") String authorizationHeader, @RequestParam(value = "jobId") Long jobId) {
        try {
            jobService.applyJob(authorizationHeader, jobId);
            return ResponseEntity.status(HttpStatus.OK).body("Applied to job, good luck!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addJob(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody JobPageDTO jobPageDTO) {
        try {
            jobService.addJob(authorizationHeader, jobPageDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Job added!");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    @PutMapping("/editJob")
    public ResponseEntity<?> updateJob (
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Job job,
            @RequestParam  Long jobId) {
        try {
            jobService.editJob(authorizationHeader, job, jobId);
            return ResponseEntity.status(HttpStatus.OK).body("Job updated!");
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(e.getMessage());
        }
    }

    @PostMapping("/favorite")
    public ResponseEntity<?> addJobToFavorites(@RequestHeader(value = "Authorization") String authorizationHeader, @RequestParam(value = "jobId") Long jobId, @RequestParam boolean isFavorite) {
        try {
            isFavorite = jobService.addJobToFavorites(authorizationHeader, jobId);
            if (!isFavorite) {
                return ResponseEntity.status(HttpStatus.OK).body("Job added to favorites!");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Job removed from favorites!");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/location")
    public ResponseEntity<List<JobListViewTypeDTO>> getJobsByLocation(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                                      @RequestParam String location) {
        try {
            List<JobListViewTypeDTO> filteredJobs = jobService.getJobsByLocation(authorizationHeader, location);
            return ResponseEntity.status(HttpStatus.OK).body(filteredJobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/jobType")
    public ResponseEntity<List<JobListViewTypeDTO>> getJobsByType(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                                  @RequestParam String jobType) {
        try {
            List<JobListViewTypeDTO> filteredJobs = jobService.getJobsByType(authorizationHeader, jobType);
            return ResponseEntity.status(HttpStatus.OK).body(filteredJobs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/jobDetails")
    public ResponseEntity<JobPageDTO> getJobDetails(@RequestHeader(value = "Authorization") String token, @RequestParam Long id) {
        JobPageDTO jobDetails = jobService.getJobPageById(token, id);
        return  ResponseEntity.status(HttpStatus.OK).body(jobDetails);
    }
}
