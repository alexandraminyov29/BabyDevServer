package com.babydev.app.service.impl;

import com.babydev.app.domain.dto.qualifications.SkillDTO;
import com.babydev.app.domain.entity.Job;
import com.babydev.app.domain.entity.Skill;
import com.babydev.app.helper.FormatUtil;
import com.babydev.app.repository.JobRepository;
import com.babydev.app.repository.qualifications.SkillRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SkillService {

    @Autowired
    private final SkillRepository skillRepository;
    @Autowired
    JobService jobService;
    @Autowired
    private final JobRepository jobRepository;

    public List<Skill> getSkills() { return skillRepository.findAll(); }

    public Skill getSkill(Long id) { return skillRepository.findById(id).get(); }

    public void deleteSkill(Long id) { skillRepository.deleteById(id); }

    public SkillDTO addSkill(long jobId, SkillDTO skill) {

        Optional<Job> job = jobRepository.findById(jobId);
        if(job.isEmpty()) {
            throw new EntityNotFoundException("Couldn't find job");
        }

        Skill newSkill = Skill.builder()
                .skillName(FormatUtil.getSkillFromEnumValue(skill.getSkillName()))
                .skillExperience(skill.getSkillExperience())
                .job(job.get())
                .build();

        job.get().getRequiredSkill().add(newSkill);
        jobRepository.save(job.get());

        return skill;
    }
}
