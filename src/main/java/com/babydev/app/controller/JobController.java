package com.babydev.app.controller;

import com.babydev.app.domain.dto.JobListViewTypeDTO;
import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
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

    @PostMapping("/add")
    public ResponseEntity<Job> addJob(@RequestBody Job job, @RequestParam Long userId, @RequestParam Long companyId) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.addJob(job, userId, companyId));
    }

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
    public ResponseEntity<List<JobListViewTypeDTO>> getJobs() {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.getAllJobs());
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobListViewTypeDTO>> searchJobs(@RequestParam String keyword) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.searchJobs(keyword));
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyToJob(@RequestParam(value = "userId") Long userId, @RequestParam(value = "jobId") Long jobId){
        try{
            jobService.applyJob(userId, jobId);
            return ResponseEntity.status(HttpStatus.OK).body("Applied to job, good luck!");
        }catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
