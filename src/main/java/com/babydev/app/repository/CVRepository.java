package com.babydev.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babydev.app.domain.entity.CurriculumVitae;

public interface CVRepository extends JpaRepository<CurriculumVitae, Long>{

}
