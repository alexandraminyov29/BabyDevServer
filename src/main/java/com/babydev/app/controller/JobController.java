package com.babydev.app.controller;

import com.babydev.app.domain.dto.JobPageDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.service.impl.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/add")
    public ResponseEntity<Job> addJob(@RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.OK).body(jobService.addJob(job));
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
}
