package com.babydev.app.controller;

import com.babydev.app.domain.dto.qualifications.SkillDTO;
import com.babydev.app.service.impl.QualificationsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    @Autowired
    QualificationsService qualificationsService;

    @PostMapping(value = "/add")
    @Transactional
    public ResponseEntity<?> addSkillToJob (@RequestParam long jobId, @RequestBody SkillDTO skill) {
        return ResponseEntity.status(HttpStatus.OK).body(qualificationsService.addSkillToJob(jobId, skill));
    }
}
