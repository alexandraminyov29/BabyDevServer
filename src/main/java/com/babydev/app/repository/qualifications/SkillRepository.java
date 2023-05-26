package com.babydev.app.repository.qualifications;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babydev.app.domain.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {

}
